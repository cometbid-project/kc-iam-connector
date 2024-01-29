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
