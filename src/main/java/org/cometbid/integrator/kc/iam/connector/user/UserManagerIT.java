/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import org.cometbid.integrator.kc.iam.connector.exception.ConflictException;

/**
 *
 * @author samueladebowale
 */
public sealed interface UserManagerIT permits UserManager {

    CreateUserResponse createUser(CreateUserRequest createUserRequest)
            throws NotFoundException, ConflictException, ClientErrorException, IOException;

    void assignUserToGroup(String username, final String groupId) throws ClientErrorException, NotFoundException, IOException;

    void removeUserFromGroup(String username, final String groupId) throws ClientErrorException, NotFoundException, IOException;

    void assignUserToRealmRole(String username, String roleName) throws ClientErrorException, NotFoundException, IOException;

    void assignUserToClientRole(String username, String clientId, String roleName) throws ClientErrorException, NotFoundException, IOException;

    void updateDetailsByUsername(final UpdateUserRequest userDetails, final String username) throws IOException;

    void updateDetailsById(final UpdateUserRequest userDetails, final String userId) throws IOException;

    void disableProfile(final String username) throws ClientErrorException, NotFoundException, IOException;

    void enableProfile(final String username) throws ClientErrorException, NotFoundException, IOException;

    boolean isMfaEnabled(String username) throws ClientErrorException, NotFoundException, IOException;

    boolean isTotpValid(final CachedCredentials cachedCredentials, final String totpCode)
            throws ClientErrorException, NotFoundException, IOException;

    void expirePasswordByUsername(final String username) throws ClientErrorException, NotFoundException, IOException;

    void expirePasswordById(final String userId) throws ClientErrorException, NotFoundException, IOException;

    void changePassword(final String username, String newPlainTextPassword) throws ClientErrorException, NotFoundException, IOException;

    void emailVerificationByUsername(final String username) throws ClientErrorException, NotFoundException, IOException;

    void emailVerificationById(final String userId) throws ClientErrorException, NotFoundException, IOException;

    void setEmailAsVerified(final String username) throws ClientErrorException, NotFoundException, IOException;

    void setEmailAsUnVerified(final String username) throws ClientErrorException, NotFoundException, IOException;

    void doForgotPasswordUsername(final String username) throws ClientErrorException, NotFoundException, IOException;

    void doForgotPasswordById(final String userId) throws ClientErrorException, NotFoundException, IOException;

    void deleteByUsername(String username) throws ConflictException, ClientErrorException, IOException;

    void deleteById(String userId) throws ConflictException, ClientErrorException, IOException;

}
