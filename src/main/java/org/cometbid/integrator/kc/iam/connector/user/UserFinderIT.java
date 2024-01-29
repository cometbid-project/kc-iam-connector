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
package org.cometbid.integrator.kc.iam.connector.user;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.cometbid.integrator.kc.iam.connector.common.model.PagingModel;

/**
 *
 * @author samueladebowale
 */
public sealed interface UserFinderIT permits UserFinder {

    List<KeycloakUser> searchUser(SearchUserCriteria searchCriteria, final PagingModel pageModel) throws IOException;

    KeycloakUser findById(String userId) throws ClientErrorException, NotFoundException, IOException;

    KeycloakUser findByUsername(String username) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByEmail(String email, final PagingModel pageModel) throws ClientErrorException, NotFoundException, IOException;

    KeycloakUser findByExactMatchingEmail(String email) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByFirstName(final String firstName) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByExactMatchingFirstName(final String firstName) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByLastName(final String lastName) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByExactMatchingLastName(final String lastName) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findAll(final PagingModel pageModel) throws IOException;

    List<KeycloakUser> findByEmailVerificationStatus(boolean emailVerified, final PagingModel pageModel) throws IOException;
    
    List<String> findUserRealmRoles(final String username) throws ClientErrorException, NotFoundException, IOException;
    
    List<String> findUserGroups(final String username) throws ClientErrorException, NotFoundException, IOException;
    
    Map<String, List<String>> findUserClientRoles(final String username) throws ClientErrorException, NotFoundException, IOException;
}
