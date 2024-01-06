/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.resource;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RoleByIdResource;

/**
 *
 * @author samueladebowale
 */
public class KeycloakRoleResource {

    private final KeycloakRealmResource keycloakRealmResource;
    private final KeycloakClientResource keycloakClientResource;

    public KeycloakRoleResource(KeycloakRealmResource keycloakRealmResource, KeycloakClientResource keycloakClientResource) {
        this.keycloakRealmResource = keycloakRealmResource;
        this.keycloakClientResource = keycloakClientResource;
    }

    /**
     *
     * @param roleName
     * @return
     * @throws java.io.IOException
     */
    public RoleRepresentation getRealmRoleRepresentation(String roleName)
            throws ClientErrorException, NotFoundException, IOException {
        // Get realm role "tester" (requires view-realm role)

        RolesResource rolesResource = getRolesResource();

        RoleResource roleResource = rolesResource.get(roleName);

        return roleResource.toRepresentation();
    }

    /**
     *
     * @param app1Client
     * @param roleName
     * @return
     * @throws java.io.IOException
     */
    public RoleRepresentation getClientRoleRepresentation(ClientRepresentation app1Client, String roleName)
            throws ClientErrorException, NotFoundException, IOException {

        ClientsResource clientsResource = this.keycloakClientResource.getClientsResource();

        // Get client level role (requires view-clients role)
        ClientResource clientResource = clientsResource.get(app1Client.getId());

        RolesResource rolesResource = clientResource.roles();

        return rolesResource.get(roleName).toRepresentation();
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public RolesResource getRolesResource()
            throws ClientErrorException, NotFoundException, IOException {

        return this.keycloakRealmResource.realmResource().roles();
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public RoleByIdResource getRolesById()
            throws ClientErrorException, NotFoundException, IOException {

        return this.keycloakRealmResource.realmResource().rolesById();
    }

}
