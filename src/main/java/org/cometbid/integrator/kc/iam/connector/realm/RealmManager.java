/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import org.keycloak.representations.idm.*;
import lombok.extern.log4j.Log4j2;
import static org.cometbid.integrator.kc.iam.connector.error.ErrorHandler.*;
import org.cometbid.integrator.kc.iam.connector.exception.ConflictException;
import org.cometbid.integrator.kc.iam.connector.resource.KeycloakRealmResource;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public final class RealmManager implements RealmManagerIT {

    private final KeycloakRealmResource keycloakRealmResource;

    public RealmManager(KeycloakRealmResource keycloakRealmResource) {
        this.keycloakRealmResource = keycloakRealmResource;
    }

    /**
     *
     * @param realmName
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public void createRealm(String realmName) throws ConflictException, ClientErrorException, IOException {

        try {
            RealmRepresentation realmRepresentation = new RealmRepresentation();
            realmRepresentation.setRealm(realmName);
            realmRepresentation.setEnabled(Boolean.TRUE);
            keycloakRealmResource.keycloak().realms().create(realmRepresentation);

            log.debug("{} was created.", realmName);
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                String errorMessage
                        = """
                          Realm '%s' has already been created.""".formatted(realmName);

                throwConflictException(errorMessage);
            } else {
                handleClientErrorException(e);
            }
        } catch (Exception e) {
            String errorMessage
                    = """
                    Failed to create realm: %s""".formatted(e.getMessage());

            handleException(e, errorMessage);
        }
    }

    /**
     *
     * @param customRealmRepresentation
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public void createRealm(RealmRepresentation customRealmRepresentation)
            throws ConflictException, ClientErrorException, IOException {

        try {
            keycloakRealmResource.keycloak().realms().create(customRealmRepresentation);

            log.debug("{} was created.", customRealmRepresentation.getRealm());
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                String errorMessage
                        = """
                          Realm '%s' has already been created.""".formatted(customRealmRepresentation.getRealm());

                throwConflictException(errorMessage);
            } else {
                handleClientErrorException(e);
            }
        } catch (Exception e) {

            String errorMessage
                    = """
                    Failed to create realm: %s""".formatted(e.getMessage());

            handleException(e, errorMessage);
        }
    }

    /**
     *
     * @param realmName
     * @throws NotFoundException
     * @throws java.io.IOException
     */
    @Override
    public void resetRealm(String realmName) throws NotFoundException, ClientErrorException, IOException {
        try {
            this.keycloakRealmResource.realmResource().remove();

            log.debug("Realm: {} was deleted.", realmName);
        } catch (Exception e) {

            String errorMessage
                    = """
                    Failed to reset realm: %s; error-message: %s"""
                            .formatted(realmName, e.getMessage());
            handleException(e, errorMessage);
        }
    }

    /**
     *
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void clearRealmKeysCache() throws ClientErrorException, NotFoundException, IOException {
        this.keycloakRealmResource.realmResource().clearKeysCache();
    }

    /**
     *
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void clearRealmCache() throws ClientErrorException, NotFoundException, IOException {
        this.keycloakRealmResource.realmResource().clearRealmCache();
    }

    /**
     *
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void clearRealmUserCache() throws ClientErrorException, NotFoundException, IOException {
        this.keycloakRealmResource.realmResource().clearUserCache();
    }

    /**
     *
     * @param groupId
     * @throws ConflictException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public void makeDefaultGroup(String groupId) throws ConflictException, ClientErrorException, IOException {
        this.keycloakRealmResource.realmResource().addDefaultGroup(groupId);
    }

    /**
     *
     * @param groupId
     * @throws ConflictException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public void deleteDefaultGroup(String groupId) throws ConflictException, ClientErrorException, IOException {
        this.keycloakRealmResource.realmResource().removeDefaultGroup(groupId);
    }

    /**
     *
     * @param realmName
     * @throws NotFoundException
     * @throws java.io.IOException
     */
    @Override
    public void logoutAll(String realmName) throws NotFoundException, ClientErrorException, IOException {
        try {
            this.keycloakRealmResource.realmResource().logoutAll();

            log.debug("All users were logged out of realm: {}.", realmName);
        } catch (Exception e) {

            String errorMessage
                    = """
                    Failed to log out all users of realm: %s; error-message: %s"""
                            .formatted(realmName, e.getMessage());
            handleException(e, errorMessage);
        }
    }

}
