/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.resource;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.representations.idm.GroupRepresentation;

/**
 *
 * @author samueladebowale
 */
public class KeycloakGroupResource {

    private final KeycloakRealmResource keycloakRealmResource;

    public KeycloakGroupResource(KeycloakRealmResource keycloakRealmResource) {
        this.keycloakRealmResource = keycloakRealmResource;
    }

    public KeycloakRealmResource realmResource() {
        return keycloakRealmResource;
    }

    /**
     *
     * @param groupId
     * @return
     * @throws java.io.IOException
     */
    public GroupRepresentation getGroupRepresentation(String groupId)
            throws ClientErrorException, NotFoundException, IOException {
        // Get realm group "tester" (requires view-realm role)

        GroupResource groupResource = getGroupResource(groupId);

        return groupResource.toRepresentation();
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public GroupsResource getGroupsResource()
            throws ClientErrorException, NotFoundException, IOException {

        return this.keycloakRealmResource.realmResource().groups();
    }

    /**
     *
     * @param groupId
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public GroupResource getGroupResource(String groupId)
            throws ClientErrorException, NotFoundException, IOException {

        return this.getGroupsResource().group(groupId);
    }

    /**
     *
     * @param path
     * @return 
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public GroupRepresentation findGroupByPath(String path)
            throws ClientErrorException, NotFoundException, IOException {
        
        return this.keycloakRealmResource.realmResource().getGroupByPath(path);
    }

}
