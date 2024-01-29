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
package org.cometbid.integrator.kc.iam.connector.resource;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

/**
 *
 * @author samueladebowale
 */
public class KeycloakUserResource {

    private final KeycloakRealmResource keycloakRealmResource;

    public KeycloakUserResource(KeycloakRealmResource keycloakRealmResource) {
        this.keycloakRealmResource = keycloakRealmResource;
    }

    public KeycloakRealmResource realmResource() {
        return keycloakRealmResource;
    }

    /**
     *
     * @param username
     * @return
     * @throws java.io.IOException
     */
    public UserResource getUserResource(String username)
            throws ClientErrorException, NotFoundException, IOException {

        UsersResource usersResource = getUsersResource();

        boolean exactMatching = true;
        List<UserRepresentation> userRepresentationList = usersResource.searchByUsername(username, exactMatching);

        if (userRepresentationList.isEmpty()) {
            String errorMessage = """
                                  User '%s' was not found.
                                  """.formatted(username);

            throw new NotFoundException(errorMessage);
        }

        String userId = userRepresentationList.get(0).getId();

        return usersResource.get(userId);
    }

    /**
     *
     * @param id
     * @return
     * @throws java.io.IOException
     */
    public UserResource getUserResourceById(String id)
            throws ClientErrorException, NotFoundException, IOException {

        return getUsersResource().get(id);
    }

    /**
     *
     * @param username
     * @return
     * @throws java.io.IOException
     */
    public UserRepresentation getUserRepresentation(String username)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = getUserResource(username);

        return userResource.toRepresentation();
    }

    /**
     *
     * @param userId
     * @return
     * @throws java.io.IOException
     */
    public UserRepresentation getUserRepresentationById(String userId)
            throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = getUserResourceById(userId);

        return userResource.toRepresentation();
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public UsersResource getUsersResource()
            throws ClientErrorException, NotFoundException, IOException {

        return this.keycloakRealmResource.realmResource().users();
    }

}
