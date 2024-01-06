/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
