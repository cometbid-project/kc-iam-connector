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
package org.cometbid.integration.kc.iam.connector.realm.client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import static org.cometbid.integration.kc.iam.connector.error.ErrorHandler.*;
import org.cometbid.integration.kc.iam.connector.exception.ConflictException;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakRealmResource;
import org.keycloak.representations.idm.ClientRepresentation;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public final class ClientManager implements ClientManagerIT {

    private final KeycloakRealmResource keycloakRealmResource;

    public ClientManager(KeycloakRealmResource keycloakRealmResource) {
        this.keycloakRealmResource = keycloakRealmResource;
    }

    /**
     *
     * @param clientName
     * @throws java.io.IOException
     */
    @Override
    public void createClientInRealm(String clientName)
            throws ConflictException, NotFoundException, ClientErrorException, IOException {

        try {
            ClientRepresentation clientRepresentation = new ClientRepresentation();
            clientRepresentation.setName(clientName);
            clientRepresentation.setEnabled(Boolean.TRUE);

            this.keycloakRealmResource.realmResource().clients().create(clientRepresentation);

            log.debug("{} was created.", clientName);
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                String errorMessage
                        = """
                          Realm Client '%s' has already been created.""".formatted(clientName);

                throwConflictException(errorMessage);
            } else {
                handleClientErrorException(e);
            }
        } catch (Exception e) {

            String errorMessage
                    = """
                    Failed to create keycloak realm client: %s""".formatted(e.getMessage());
            handleException(e, errorMessage);
        }
    }
}
