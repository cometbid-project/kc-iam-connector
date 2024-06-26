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

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserConsentRepresentation;
import org.keycloak.representations.idm.UserProfileMetadata;

/**
 *
 * @author samueladebowale
 */
@Builder
public record KeycloakUser(String id, String self, String username, String firstName,
        String lastName, String email, boolean enabled, boolean emailVerified, boolean mfaEnabled,
        long createdTimestamp,
        String origin,
        String federationLink,
        String serviceAccountClientId,
        List<SocialLink> socialLinks,
        List<String> groups,
        Map<String, Boolean> access,
        UserProfileMetadata userProfileMetadata,
        Map<String, List<String>> attributes,
        List<CredentialRepresentation> credentials,
        Set<String> disableableCredentialTypes,
        List<String> requiredActions,
        List<FederatedIdentityRepresentation> federatedIdentities,
        List<String> realmRoles,
        Map<String, List<String>> clientRoles,
        List<UserConsentRepresentation> clientConsents,
        Integer notBefore) {

    public static KeycloakUser create(String id, String username, String firstName,
            String lastName, String email) {

        return KeycloakUser.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

    public static KeycloakUser create(String id, String username, String firstName,
            String lastName, String email, boolean enabled, boolean emailVerified, boolean mfaEnabled) {

        return KeycloakUser.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .enabled(enabled)
                .emailVerified(emailVerified)
                .mfaEnabled(mfaEnabled)
                .build();
    }

    public static KeycloakUser create(String username, String firstName,
            String lastName, String email) {

        return KeycloakUser.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

}
