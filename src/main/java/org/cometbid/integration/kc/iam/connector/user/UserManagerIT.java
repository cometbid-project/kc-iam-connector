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
import java.io.IOException;
import org.cometbid.integration.kc.iam.connector.exception.ConflictException;

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
