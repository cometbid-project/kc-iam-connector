/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.resource;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import org.keycloak.admin.client.resource.ClientScopeResource;
import org.keycloak.admin.client.resource.ClientScopesResource;
import org.keycloak.representations.idm.ClientScopeRepresentation;

/**
 *
 * @author samueladebowale
 */
public class KeycloakClientScopesResource {

    private final KeycloakRealmResource keycloakRealmResource;

    public KeycloakClientScopesResource(KeycloakRealmResource keycloakRealmResource) {
        this.keycloakRealmResource = keycloakRealmResource;
    }

    /**
     *
     * @param uuid
     * @return
     * @throws java.io.IOException
     */
    public ClientScopeRepresentation getClientRepresentation(String uuid) 
                        throws NotFoundException, ClientErrorException, IOException {

        // Get client
        ClientScopeResource clientScopeResource = getClientScopeResource(uuid);
        //
        return clientScopeResource.toRepresentation();
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public ClientScopesResource getClientScopesResource() 
                        throws ClientErrorException, NotFoundException, IOException {

        return this.keycloakRealmResource.realmResource().clientScopes();
    }

    /**
     *
     * @param uuid clientScope UUID 
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public ClientScopeResource getClientScopeResource(String uuid) 
                            throws ClientErrorException, NotFoundException, IOException {

        return this.getClientScopesResource().get(uuid);
    }
}
