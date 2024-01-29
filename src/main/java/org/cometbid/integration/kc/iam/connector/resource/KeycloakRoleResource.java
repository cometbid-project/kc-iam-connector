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
package org.cometbid.integration.kc.iam.connector.resource;

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
