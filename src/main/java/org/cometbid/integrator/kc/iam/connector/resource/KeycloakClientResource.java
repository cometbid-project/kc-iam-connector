/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.resource;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;

/**
 *
 * @author samueladebowale
 */
public class KeycloakClientResource {

    private final KeycloakRealmResource keycloakRealmResource;

    public KeycloakClientResource(KeycloakRealmResource keycloakRealmResource) {
        this.keycloakRealmResource = keycloakRealmResource;
    }

    /**
     *
     * @param clientId
     * @return
     * @throws java.io.IOException
     */
    public ClientRepresentation getClientRepresentation(String clientId)
            throws NotFoundException, ClientErrorException, IOException {

        // Get client
        ClientsResource clientsResource = getClientsResource();
        //
        List<ClientRepresentation> clientRepresentationList = clientsResource.findByClientId(clientId);

        if (clientRepresentationList.isEmpty()) {
            String errorMessage = """
                                  Client with id '%s' was not found.
                                  """.formatted(clientId);

            throw new NotFoundException(errorMessage);
        }

        return clientRepresentationList.get(0);
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public ClientsResource getClientsResource()
            throws ClientErrorException, NotFoundException, IOException {

        return this.keycloakRealmResource.realmResource().clients();
    }

    /**
     *
     * @param uuid client UUID and not clientID
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public ClientResource getClientResource(String uuid)
            throws ClientErrorException, NotFoundException, IOException {

        return this.getClientsResource().get(uuid);
    }
}
