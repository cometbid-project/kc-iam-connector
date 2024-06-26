/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.integration.kc.iam.connector.user;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import org.keycloak.representations.idm.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.lang3.StringUtils;
import org.cometbid.integration.kc.iam.connector.config.PasswordConfigProperties;
import org.cometbid.integration.kc.iam.connector.enums.ProfileStatus;
import static org.cometbid.integration.kc.iam.connector.enums.ProfileStatus.PASSWORD_EXPIRED;
import static org.cometbid.integration.kc.iam.connector.enums.ProfileStatus.PROFILE_EXPIRED;
import static org.cometbid.integration.kc.iam.connector.enums.ProfileStatus.PROFILE_LOCKED;
import static org.cometbid.integration.kc.iam.connector.enums.ProfileStatus.PROFILE_ACTIVE;
import static org.cometbid.integration.kc.iam.connector.error.ErrorHandler.*;
import org.cometbid.integration.kc.iam.connector.exception.ConflictException;
import org.cometbid.integration.kc.iam.connector.exception.CredentialAlreadyUsedException;
import org.cometbid.integration.kc.iam.connector.realm.role.Role;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakClientResource;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakGroupResource;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakPasswordResource;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakRealmResource;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakRoleResource;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakUserResource;
import static org.cometbid.integration.kc.iam.connector.util.Constants.TOTP_SECRET;
import org.cometbid.integration.kc.iam.connector.totp.TotpManager;
import static org.cometbid.integration.kc.iam.connector.util.Constants.RECOVERY_CODES;
import static org.cometbid.integration.kc.iam.connector.util.Constants.UPDATE_PASSWORD;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public final class UserManager implements UserManagerIT {

    private final KeycloakUserResource keycloakUserResource;
    private final KeycloakRoleResource keycloakRoleResource;
    private final KeycloakPasswordResource keycloakPasswordResource;
    private final KeycloakGroupResource keycloakGroupResource;
    private final KeycloakClientResource keycloakClientResource;

    private final TotpManager totpManager;
    private final PasswordConfigProperties configProp;

    @Builder
    public UserManager(KeycloakRealmResource keycloakRealmResource,
            PasswordEncoder passwordEncoder,
            PasswordConfigProperties configProp, TotpManager totpManager) {

        this.totpManager = totpManager;
        this.configProp = configProp;

        this.keycloakClientResource = new KeycloakClientResource(keycloakRealmResource);
        this.keycloakUserResource = new KeycloakUserResource(keycloakRealmResource);
        this.keycloakGroupResource = new KeycloakGroupResource(keycloakRealmResource);

        this.keycloakRoleResource = new KeycloakRoleResource(keycloakRealmResource, this.keycloakClientResource);
        if (passwordEncoder == null) {
            this.keycloakPasswordResource = new KeycloakPasswordResource();
        } else {
            this.keycloakPasswordResource = new KeycloakPasswordResource(passwordEncoder);
        }
    }

    /**
     *
     * @param createUserRequest
     * @return
     * @throws ConflictException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest)
            throws NotFoundException, ConflictException, ClientErrorException, IOException {

        KeycloakUser keycloakUser = createUserRequest.keycloakUser();
        List<String> recoveryCodelist = createUserRequest.recoveryCodelist();
        Set<String> roles = createUserRequest.roles();
        ProfileStatus profileStatus = createUserRequest.profileStatus();
        String plainPassword = createUserRequest.plainPassword();

        try {
            String username = keycloakUser.username();

            CredentialRepresentation credentialRep = createPasswordRepresentation(plainPassword);
            List<SocialLinkRepresentation> socialLinkRepresentation = createSocialLinkRepresentations(keycloakUser.socialLinks());
            UserRepresentation userRepresentation = createUserRepresentation(keycloakUser, recoveryCodelist, roles,
                    profileStatus, credentialRep, socialLinkRepresentation);

            Response response = this.keycloakUserResource.getUsersResource().create(userRepresentation);

            log.info("Response: {} {}", response.getStatus(), response.getStatusInfo());

            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                log.debug("{} was created.", username);

                if (createUserRequest.keycloakUser().mfaEnabled()) {
                    MfaToken mfaToken = enableMfa(username);
                    return CreateUserResponse.createSuccessResponse(mfaToken);
                } else {
                    disableMfa(username);
                    return CreateUserResponse.createSuccessResponse(null);
                }

            } else if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                throwConflictException("""
                                      User '%s' has already been created.
                                       """.formatted(username));
            } else {

                throwUnexpectedStatusException("Unexpected response status", response.getStatus());
            }
        } catch (Exception e) {
            String errorMessage
                    = """
                    Failed to create keycloak realm user: %s""".formatted(e.getMessage());

            handleException(e, errorMessage);
        }

        // runtime not expected to get here
        return CreateUserResponse.createCompletedResponse();
    }

    /**
     *
     * @param cachedCredentials
     * @param totpCode
     * @return
     * @throws java.io.IOException
     */
    @Override
    public boolean isTotpValid(final CachedCredentials cachedCredentials, final String totpCode)
            throws ClientErrorException, NotFoundException, IOException {
        String otpCode = cachedCredentials.otpCode();
        String username = cachedCredentials.username();

        UserRepresentation userRepresentation = this.keycloakUserResource.getUserRepresentation(username);

        List<String> totpSecret = userRepresentation.getAttributes().getOrDefault(TOTP_SECRET, new ArrayList<>());

        if (StringUtils.isNotBlank(totpCode) && !totpSecret.isEmpty()) {
            return totpManager.validateCode(totpCode, totpSecret.get(0));
        } else if (StringUtils.isNotBlank(otpCode)) {
            // Compare cache entry with totp code sent
            // Used when otp was sent via text message or email
            return otpCode.equals(totpCode);
        } else {
            if (StringUtils.isBlank(totpCode)) {
                return false;
            }

            List<String> recoveryCodes = userRepresentation.getAttributes().getOrDefault(RECOVERY_CODES, new ArrayList<>());

            return recoveryCodes.stream().anyMatch(recCode -> recCode.equalsIgnoreCase(totpCode));
        }
    }

    /**
     *
     * @param username
     * @return
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public boolean isMfaEnabled(String username) throws ClientErrorException, NotFoundException, IOException {

        UserRepresentation userRepresentation = this.keycloakUserResource.getUserRepresentation(username);

        return userRepresentation.isTotp();
    }

    /**
     *
     * @param username
     * @param groupId
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void assignUserToGroup(String username, final String groupId) throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);
        GroupRepresentation groupRepresentation = keycloakGroupResource.getGroupRepresentation(groupId);

        userResource.joinGroup(groupRepresentation.getId());
    }

    /**
     *
     * @param username
     * @param groupId
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void removeUserFromGroup(String username, final String groupId) throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);
        GroupRepresentation groupRepresentation = keycloakGroupResource.getGroupRepresentation(groupId);

        userResource.leaveGroup(groupRepresentation.getId());
    }

    /**
     *
     * @param username
     * @param roleName
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void assignUserToRealmRole(String username, String roleName)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        RoleRepresentation roleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(roleName);

        userResource.roles().realmLevel().add(List.of(roleRepresentation));
    }

    /**
     *
     * @param username
     * @param clientId
     * @param roleName
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void assignUserToClientRole(String username, final String clientId, final String roleName)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        ClientRepresentation clientRepresentation = keycloakClientResource.getClientRepresentation(clientId);

        RoleRepresentation roleRepresentation = this.keycloakRoleResource.getClientRoleRepresentation(clientRepresentation, roleName);

        userResource.roles().clientLevel(clientRepresentation.getId()).add(List.of(roleRepresentation));
    }

    /**
     * Update User details
     *
     * @param userDetails
     * @param userId
     * @throws java.io.IOException
     */
    @Override
    public void updateDetailsById(final UpdateUserRequest userDetails, final String userId) throws IOException {

        UserResource userResource = this.keycloakUserResource.getUserResourceById(userId);

        doUserDetailsUpdate(userResource, userDetails);
    }

    /**
     *
     * @param userDetails
     * @param username
     * @throws IOException
     */
    @Override
    public void updateDetailsByUsername(final UpdateUserRequest userDetails, final String username) throws IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        doUserDetailsUpdate(userResource, userDetails);
    }

    private void doUserDetailsUpdate(UserResource userResource, UpdateUserRequest userDetails) {
        // Create the user representation
        UserRepresentation userRepresentation = userResource.toRepresentation();

        userRepresentation.setLastName(userDetails.lastName());
        userRepresentation.setFirstName(userDetails.firstName());

        userResource.update(userRepresentation);
    }

    /**
     *
     * @param username
     * @throws java.io.IOException
     */
    @Override
    public void expirePasswordByUsername(final String username) throws ClientErrorException, NotFoundException, IOException {

        log.info("To expire password for: {}", username);

        UserRepresentation userRepresentation = this.keycloakUserResource.getUserRepresentation(username);
        userRepresentation.singleAttribute(PASSWORD_EXPIRED.getProfileStatus(), Boolean.TRUE.toString());

        UserResource userResource = this.keycloakUserResource.getUserResource(username);
        userResource.update(userRepresentation);
    }

    /**
     *
     * @param userId
     * @throws java.io.IOException
     */
    @Override
    public void expirePasswordById(final String userId) throws ClientErrorException, NotFoundException, IOException {

        log.info("To expire password for: {}", userId);

        UserRepresentation userRepresentation = this.keycloakUserResource.getUserRepresentationById(userId);
        userRepresentation.singleAttribute(PASSWORD_EXPIRED.getProfileStatus(), Boolean.TRUE.toString());

        UserResource userResource = this.keycloakUserResource.getUserResourceById(userId);
        userResource.update(userRepresentation);
    }

    /**
     *
     * @param username
     * @param newPlainTextPassword
     * @throws java.io.IOException
     */
    @Override
    public void changePassword(final String username, String newPlainTextPassword)
            throws ClientErrorException, NotFoundException, IOException {

        log.info("To change password for: {}", username);

        UserResource userResource = keycloakUserResource.getUserResource(username);
        UserRepresentation userRepresentation = userResource.toRepresentation();

        //String hashedPassword = retrieveCurrentPassword(userRepresentation);
        Set<String> usedPasswords = userRepresentation.getDisableableCredentialTypes();

        if (!configProp.allowPasswordReuse()) {
            if (keycloakPasswordResource.isPasswordUsedBefore(usedPasswords, newPlainTextPassword)) {
                throw new CredentialAlreadyUsedException("Password previously used, and reuse is currently disabled");
            }
        }

        Set<String> newCredentialsList = addToUsedPassword(usedPasswords, newPlainTextPassword);
        userRepresentation.setDisableableCredentialTypes(newCredentialsList);

        // Assumption: Successful change of password make Expired Accounts change to
        userRepresentation.singleAttribute(PASSWORD_EXPIRED.getProfileStatus(), Boolean.FALSE.toString());

        CredentialRepresentation credential = createPasswordRepresentation(newPlainTextPassword);
        userRepresentation.setCredentials(Arrays.asList(credential));

        userResource.resetPassword(credential);
        userResource.update(userRepresentation);
    }

    private String retrieveCurrentPassword(UserRepresentation userRepresentation) {
        // Get Old Password 
        List<CredentialRepresentation> credentialList = userRepresentation.getCredentials();
        CredentialRepresentation currentCredential = credentialList.get(0);
        return currentCredential.getValue();
    }

    private Set<String> addToUsedPassword(Set<String> pastCredentials, String newPlainPassword) {

        final int maxAllowedPassword = configProp.numberOfPastPassword();

        Collection<String> passwdHistory = pastCredentials;
        log.info("Password history {}", passwdHistory);

        String encodedPassword = keycloakPasswordResource.hashPassword(newPlainPassword);
        log.info("Encoded password {}", encodedPassword);

        if (passwdHistory.size() >= maxAllowedPassword) {
            CircularFifoQueue<String> fifoQueue = new CircularFifoQueue<>(maxAllowedPassword);
            fifoQueue.addAll(passwdHistory);
            fifoQueue.add(encodedPassword);

            passwdHistory.clear();
            passwdHistory.addAll(fifoQueue);
        } else {
            passwdHistory.add(encodedPassword);
        }

        return passwdHistory.stream().collect(Collectors.toSet());
    }

    /**
     * @param username
     * @throws java.io.IOException
     *
     */
    @Override
    public void setEmailAsVerified(final String username)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEmailVerified(Boolean.TRUE);

        userResource.update(userRepresentation);
    }

    /**
     * @param username
     * @throws java.io.IOException
     *
     */
    @Override
    public void setEmailAsUnVerified(final String username)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEmailVerified(Boolean.FALSE);

        userResource.update(userRepresentation);
    }

    /**
     * @param username
     * @throws java.io.IOException
     *
     */
    @Override
    public void emailVerificationByUsername(final String username)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        userResource.sendVerifyEmail();
    }

    /**
     * @param userId
     * @throws java.io.IOException
     *
     */
    @Override
    public void emailVerificationById(final String userId)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResourceById(userId);

        userResource.sendVerifyEmail();
    }

    /**
     *
     * @param userId
     * @throws java.io.IOException
     *
     */
    @Override
    public void doForgotPasswordById(final String userId)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResourceById(userId);

        List<String> actions = List.of(UPDATE_PASSWORD);
        userResource.executeActionsEmail(actions);
    }

    /**
     *
     * @param username
     * @throws java.io.IOException
     *
     */
    @Override
    public void doForgotPasswordUsername(final String username)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);
        List<String> actions = List.of(UPDATE_PASSWORD);
        userResource.executeActionsEmail(actions);
    }

    /**
     * @param username
     * @param newPlainTextPassword
     * @throws java.io.IOException
     *
     */
    /*
    @Override
    public void resetPassword(final String username, final String newPlainTextPassword)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        doPasswordReset(userResource, newPlainTextPassword);
    }

    private void doPasswordReset(UserResource userResource, String newPlainTextPassword) {
        // Define password credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPlainTextPassword);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        Set<String> usedPasswords = userRepresentation.getDisableableCredentialTypes();

        Set<String> newCredentialsList = addToUsedPassword(usedPasswords, newPlainTextPassword);
        userRepresentation.setDisableableCredentialTypes(newCredentialsList);

        userResource.resetPassword(credential);
        userResource.update(userRepresentation);
        // "User password was reset successful"
    }
     */
    /**
     *
     * @param username
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void disableMfa(final String username) throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        disableMFA(userRepresentation);

        userResource.update(userRepresentation);
    }

    /**
     *
     * @param username
     * @return
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public MfaToken enableMfa(final String username) throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        enableMFA(userRepresentation);

        List<String> totpSecrets = userRepresentation.getAttributes().getOrDefault(TOTP_SECRET, new ArrayList<>());
        String totpSecret = !totpSecrets.isEmpty() ? totpSecrets.get(0) : "";

        userResource.update(userRepresentation);

        //Generate the QR Code
        String qrCode = totpManager.generateQrImage(userRepresentation.getEmail(), totpSecret);

        return MfaToken.builder()
                .totpSecret(totpSecret)
                .qrCode(qrCode)
                .build();
    }

    /**
     *
     * @param username
     * @throws java.io.IOException
     */
    @Override
    public void disableProfile(final String username) throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        doProfileUpdate(userResource, Boolean.FALSE);
    }

    /**
     *
     * @param username
     * @throws java.io.IOException
     */
    @Override
    public void enableProfile(final String username) throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        doProfileUpdate(userResource, Boolean.TRUE);
    }

    private void doProfileUpdate(UserResource userResource, boolean toEnable) {

        // Create the user representation
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(toEnable);
        userResource.update(userRepresentation);
    }

    /**
     *
     * @param username
     * @throws ConflictException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public void deleteByUsername(String username) throws ConflictException, ClientErrorException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);
        userResource.remove();
    }

    /**
     *
     * @param userId
     * @throws ConflictException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public void deleteById(String userId) throws ConflictException, ClientErrorException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResourceById(userId);
        userResource.remove();
    }

    private SocialLinkRepresentation createSocialLinkRepresentation(SocialLink socialLink) {
        SocialLinkRepresentation socialLinkRepresentation = new SocialLinkRepresentation();
        socialLinkRepresentation.setSocialProvider(socialLink.getSocialProvider().getProviderType());
        socialLinkRepresentation.setSocialUserId(socialLink.getProviderUserId());
        socialLinkRepresentation.setSocialUsername(socialLink.getProviderUserId());

        return socialLinkRepresentation;
    }

    private List<SocialLinkRepresentation> createSocialLinkRepresentations(List<SocialLink> socialLinks) {

        return socialLinks.stream().map(this::createSocialLinkRepresentation).collect(Collectors.toList());
    }

    private UserRepresentation createUserRepresentation(KeycloakUser keycloakUser, List<String> recoveryCodelist,
            Set<String> roles, ProfileStatus profileStatus,
            CredentialRepresentation credRepresentation, List<SocialLinkRepresentation> socialLinkRepresentations)
            throws ClientErrorException, NotFoundException, IOException {

        // Define the user
        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername(keycloakUser.username());
        newUser.setFirstName(keycloakUser.firstName());
        newUser.setLastName(keycloakUser.lastName());
        newUser.setCreatedTimestamp(Instant.now().getEpochSecond());
        newUser.setRequiredActions(new ArrayList<>());
        newUser.setEmail(newUser.getEmail());
        newUser.setEnabled(Boolean.TRUE);
        newUser.setEmailVerified(keycloakUser.emailVerified());
        newUser.setCredentials(Arrays.asList(credRepresentation));

        List<String> roleNames = this.getRoleNames(roles);
        newUser.setRealmRoles(roleNames);

        newUser.setGroups(List.of());
        newUser.setClientRoles(Map.of());
        newUser.setSocialLinks(socialLinkRepresentations);

        //String[] recoveryCodes = keycloakUser.recoveryCodes();
        if (CollectionUtils.isNotEmpty(recoveryCodelist)) {
            //List<String> recoveryCodelist = Arrays.asList(recoveryCodes);
            Map<String, List<String>> attributesMap = newUser.getAttributes();
            attributesMap.put(RECOVERY_CODES, recoveryCodelist);
        }

        if (keycloakUser.mfaEnabled()) {
            enableMFA(newUser);
        } else {
            disableMFA(newUser);
        }

        if (profileStatus == PROFILE_ACTIVE) {
            newUser.singleAttribute(PROFILE_ACTIVE.getProfileStatus(), Boolean.TRUE.toString());
        } else {
            switch (profileStatus) {
                case PROFILE_LOCKED:
                    // Only Admin can unlock
                    newUser.singleAttribute(PROFILE_LOCKED.getProfileStatus(), Boolean.TRUE.toString());
                    break;
                case PROFILE_EXPIRED:
                    // Require change of Credentials
                    newUser.singleAttribute(PROFILE_EXPIRED.getProfileStatus(), Boolean.TRUE.toString());
                    break;
                default:
                    newUser.singleAttribute(PROFILE_ACTIVE.getProfileStatus(), Boolean.TRUE.toString());
                    break;
            }
        }

        Set<String> pastCredentials = newUser.getDisableableCredentialTypes();
        if (CollectionUtils.isEmpty(pastCredentials)) {
            pastCredentials = new HashSet<>();
        }
        // add password to history
        pastCredentials.add(credRepresentation.getValue());
        newUser.setDisableableCredentialTypes(pastCredentials);

        return newUser;
    }

    // To verify if the roles specified are existing roles
    private List<String> getRealmRoleNames(List<Role> userRoles) throws ClientErrorException, NotFoundException, IOException {

        List<String> newList = new ArrayList<>();

        for (Role role : userRoles) {
            RoleRepresentation roleRepresentation = keycloakRoleResource.getRealmRoleRepresentation(role.name());
            newList.add(roleRepresentation.getName());
        }
        return newList;
    }

    // To verify if the roles specified are existing roles
    private List<String> getRoleNames(Set<String> userRoles) throws ClientErrorException, NotFoundException, IOException {

        List<String> newList = new ArrayList<>();

        for (String roleName : userRoles) {
            RoleRepresentation roleRepresentation = keycloakRoleResource.getRealmRoleRepresentation(roleName);
            newList.add(roleRepresentation.getName());
        }
        return newList;
    }

    private CredentialRepresentation createPasswordRepresentation(String plainPassword) {
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(Boolean.FALSE);
        credentials.setType(CredentialRepresentation.PASSWORD);

        String hashedPassword = this.keycloakPasswordResource.hashPassword(plainPassword);
        credentials.setValue(hashedPassword);
        credentials.setCreatedDate(Instant.now().getEpochSecond());

        return credentials;
    }

    private void enableMFA(UserRepresentation newUser) {
        String totpSecret = this.totpManager.generateSecret();

        newUser.singleAttribute(TOTP_SECRET, totpSecret);
        newUser.setTotp(Boolean.TRUE);
    }

    private void disableMFA(UserRepresentation newUser) {

        newUser.singleAttribute(TOTP_SECRET, null);
        newUser.setTotp(Boolean.FALSE);
    }
}
