/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import static org.cometbid.integrator.kc.iam.connector.error.ErrorHandler.*;
import org.cometbid.integrator.kc.iam.connector.exception.ConflictException;
import org.cometbid.integrator.kc.iam.connector.resource.KeycloakRealmResource;
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
