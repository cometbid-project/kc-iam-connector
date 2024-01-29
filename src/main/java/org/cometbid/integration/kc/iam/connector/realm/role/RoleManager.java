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
package org.cometbid.integration.kc.iam.connector.realm.role;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.Assert;
import static org.cometbid.integration.kc.iam.connector.error.ErrorHandler.*;
import org.cometbid.integration.kc.iam.connector.exception.ConflictException;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakClientResource;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakRoleResource;
import static org.cometbid.integration.kc.iam.connector.util.Constants.ROLE_PREFIX;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.keycloak.admin.client.resource.ClientResource;

/**
 * Next version: org.​keycloak.​admin.​client.​resource.​RoleByIdResource
 * 
 * deleteComposites
 * getClientRoleComposites
 * getRealmRoleComposites
 * getRoleComposites
 * searchRoleComposites
 *
 * @author samueladebowale
 */
@Log4j2
public final class RoleManager implements RoleManagerIT {

    private final KeycloakRoleResource keycloakRoleResource;
    private final KeycloakClientResource keycloakClientResource;

    public RoleManager(KeycloakRoleResource keycloakRoleResource,
            KeycloakClientResource keycloakClientResource) {
        this.keycloakRoleResource = keycloakRoleResource;
        this.keycloakClientResource = keycloakClientResource;
    }

    /**
     *
     * @param role
     * @throws ConflictException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public void createRoleInRealm(Role role)
            throws ConflictException, NotFoundException, ClientErrorException, IOException {

        Assert.requireNonEmpty(role.name(), "Role name cannot be empty.");
        Assert.requireNonEmpty(role.description(), "Role description cannot be empty.");

        final String upperRoleName = role.name().toUpperCase();
        final String realmRoleName = StringUtils.prependIfMissing(upperRoleName, ROLE_PREFIX);

        try {
            RoleRepresentation realmRoleRepresentation = new RoleRepresentation();
            realmRoleRepresentation.setName(realmRoleName);
            realmRoleRepresentation.setDescription(role.description());
            realmRoleRepresentation.setClientRole(Boolean.TRUE);

            this.keycloakRoleResource.getRolesResource().create(realmRoleRepresentation);

            log.debug("Realm role '{}' was created.", realmRoleName);
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                String errorMessage
                        = """
                          Realm Role '%s' has already been created.""".formatted(realmRoleName);

                throwConflictException(errorMessage);
            } else {
                handleClientErrorException(e);
            }
        } catch (Exception e) {
            String errorMessage
                    = """
                    Failed to create keycloak realm role: %s""".formatted(e.getMessage());

            handleException(e, errorMessage);
        }
    }

    /**
     *
     * @param clientId
     * @param role
     * @throws java.io.IOException
     */
    @Override
    public void createRoleInClient(String clientId, Role role)
            throws ConflictException, NotFoundException, ClientErrorException, IOException {

        Assert.requireNonEmpty(role.name(), "Role name cannot be empty.");
        Assert.requireNonEmpty(role.description(), "Role description cannot be empty.");

        final String upperRoleName = role.name().toUpperCase();
        final String clientRoleName = StringUtils.prependIfMissing(upperRoleName, ROLE_PREFIX);

        try {
            RoleRepresentation clientRoleRepresentation = new RoleRepresentation();
            clientRoleRepresentation.setName(clientRoleName);
            clientRoleRepresentation.setDescription(role.description());
            clientRoleRepresentation.setClientRole(Boolean.TRUE);
            clientRoleRepresentation.setComposite(Boolean.FALSE);

            ClientRepresentation clientRepresentation = this.keycloakClientResource.getClientRepresentation(clientId);
            ClientResource clientResource = this.keycloakClientResource.getClientResource(clientRepresentation.getId());

            clientResource.roles().create(clientRoleRepresentation);

            log.debug("Client role '{}' was created.", clientRoleName);
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                String errorMessage
                        = """
                         Client role '%s' has already been created.""".formatted(clientRoleName);

                throwConflictException(errorMessage);
            } else {
                handleClientErrorException(e);
            }
        } catch (Exception e) {

            String errorMessage
                    = """
                    Failed to create keycloak client role: %s""".formatted(e.getMessage());
            handleException(e, errorMessage);
        }
    }

    /**
     *
     * @param role
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void updateRole(Role role)
            throws ClientErrorException, NotFoundException, IOException {
        
        Assert.requireNonEmpty(role.name(), "Role name cannot be empty.");
        Assert.requireNonEmpty(role.description(), "Role description cannot be empty.");

        RoleRepresentation realmRoleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(role.name());
        realmRoleRepresentation.setName(role.name());
        realmRoleRepresentation.setDescription(role.description()); 

        this.keycloakRoleResource.getRolesById().updateRole(realmRoleRepresentation.getId(), realmRoleRepresentation);
    }

    /**
     *
     * @param roleName
     * @param roleAttr
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void addAttribute(String roleName, RoleAttribute roleAttr)
            throws ClientErrorException, NotFoundException, IOException {

        RoleRepresentation realmRoleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(roleName)
                .singleAttribute(roleAttr.keyname(), roleAttr.value());

        this.keycloakRoleResource.getRolesById().updateRole(realmRoleRepresentation.getId(), realmRoleRepresentation);
    }

    /**
     *
     * @param roleName
     * @param attributes
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void setAttributes(String roleName, Map<String, List<String>> attributes)
            throws ClientErrorException, NotFoundException, IOException {

        RoleRepresentation realmRoleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(roleName);
        realmRoleRepresentation.setAttributes(attributes);

        this.keycloakRoleResource.getRolesById().updateRole(realmRoleRepresentation.getId(), realmRoleRepresentation);
    }

    /**
     *
     * @param realmRoleName
     * @throws java.io.IOException
     */
    @Override
    public void deleteRealmRole(String realmRoleName)
            throws ClientErrorException, NotFoundException, IOException {

        this.keycloakRoleResource.getRolesResource().deleteRole(realmRoleName);
    }

    /**
     *
     * @param uuid
     * @param clientRoleName
     * @throws java.io.IOException
     */
    @Override
    public void deleteClientRole(String uuid, String clientRoleName)
            throws ClientErrorException, NotFoundException, IOException {

        this.keycloakClientResource.getClientResource(uuid).roles().deleteRole(clientRoleName);
    }

    /**
     *
     * @param roleToMakeComposite
     * @param childRole
     * @param createIfAbsent
     * @throws java.io.IOException
     */
    @Override
    public void makeRealmRoleComposite(final String roleToMakeComposite, final Role childRole, boolean createIfAbsent)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(roleToMakeComposite, "Realm Role name to make composite cannot be empty.");
        Assert.requireNonEmpty(childRole.name(), "Realm Role name to add as child cannot be empty.");

        if (roleToMakeComposite.equalsIgnoreCase(childRole.name())) {
            throwConflictException("""
                                      A realm role: '%s' cannot have same name '%s' as child role
                                       """.formatted(roleToMakeComposite, childRole.name()));
        }

        final String upperRoleName = roleToMakeComposite.toUpperCase();
        final String realmRoleName = StringUtils.prependIfMissing(upperRoleName, ROLE_PREFIX);

        final String childUpperRoleName = childRole.name().toUpperCase();
        final String childRealmRoleName = StringUtils.prependIfMissing(childUpperRoleName, ROLE_PREFIX);

        RoleRepresentation childRealmRoleRepresentation = null;

        try {

            childRealmRoleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(childRealmRoleName);

        } catch (NotFoundException ex) {
            if (!createIfAbsent) {
                SynchronousDispatcher.rethrow(ex);
            }
            // create the new role as realmRole
            this.createRoleInRealm(childRole);
            // Retrieve it back
            childRealmRoleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(childRealmRoleName);
        }

        RoleRepresentation realmRoleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(realmRoleName);
        realmRoleRepresentation.setComposite(Boolean.TRUE);

        List<RoleRepresentation> composites = new LinkedList<>();
        composites.add(childRealmRoleRepresentation);

        this.keycloakRoleResource.getRolesById().addComposites(realmRoleRepresentation.getId(), composites);
    }

    /**
     *
     * @param roleToMakeComposite
     * @param childRole
     * @param clientId
     * @param createIfAbsent
     * @throws java.io.IOException
     */
    @Override
    public void makeRealmRoleCompositeWithClientRole(final String roleToMakeComposite, final Role childRole,
            final String clientId, boolean createIfAbsent)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(roleToMakeComposite, "Realm Role name to make composite cannot be empty.");
        Assert.requireNonEmpty(childRole.name(), "Client Role name to add as child cannot be empty.");
        Assert.requireNonEmpty(clientId, "ClientID cannot be empty.");

        final String upperRoleName = roleToMakeComposite.toUpperCase();
        final String realmRoleName = StringUtils.prependIfMissing(upperRoleName, ROLE_PREFIX);

        final String childUpperRoleName = childRole.name().toUpperCase();
        final String childClientRoleName = StringUtils.prependIfMissing(childUpperRoleName, ROLE_PREFIX);

        // Check if clientId is valid
        ClientRepresentation clientRepresentation = this.keycloakClientResource.getClientRepresentation(clientId);

        RoleRepresentation childClientRoleRepresentation = null;

        try {

            childClientRoleRepresentation = this.keycloakRoleResource.getClientRoleRepresentation(clientRepresentation, childClientRoleName);

        } catch (NotFoundException ex) {
            if (!createIfAbsent) {
                SynchronousDispatcher.rethrow(ex);
            }
            // create the new role as realmRole
            this.createRoleInClient(clientId, childRole);
            // Retrieve it back
            childClientRoleRepresentation = this.keycloakRoleResource.getClientRoleRepresentation(clientRepresentation, childClientRoleName);
        }
        // check if the realm role is valid
        RoleRepresentation realmRoleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(realmRoleName);
        realmRoleRepresentation.setComposite(Boolean.TRUE);

        List<RoleRepresentation> composites = new LinkedList<>();
        composites.add(childClientRoleRepresentation);

        this.keycloakRoleResource.getRolesById().addComposites(realmRoleRepresentation.getId(), composites);
    }

    /**
     *
     * @param roleToMakeComposite
     * @param clientId
     * @param childRole
     * @param createIfAbsent
     * @throws java.io.IOException
     */
    @Override
    public void makeClientRoleComposite(final String roleToMakeComposite, final String clientId,
            final Role childRole, boolean createIfAbsent)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(roleToMakeComposite, "Client role name to make composite cannot be empty.");
        Assert.requireNonEmpty(childRole.name(), "Client role name to add as child cannot be empty.");
        Assert.requireNonEmpty(clientId, "ClientID cannot be empty.");

        if (roleToMakeComposite.equalsIgnoreCase(childRole.name())) {
            throwConflictException("""
                                      A client role: '%s' cannot have same name '%s' as child role
                                       """.formatted(roleToMakeComposite, childRole.name()));
        }

        final String upperRoleName = roleToMakeComposite.toUpperCase();
        final String clientRoleName = StringUtils.prependIfMissing(upperRoleName, ROLE_PREFIX);

        final String childUpperRoleName = childRole.name().toUpperCase();
        final String childClientRoleName = StringUtils.prependIfMissing(childUpperRoleName, ROLE_PREFIX);

        // Check if clientId is valid
        ClientRepresentation clientRepresentation = this.keycloakClientResource.getClientRepresentation(clientId);

        RoleRepresentation childClientRoleRepresentation = null;

        try {

            childClientRoleRepresentation = this.keycloakRoleResource.getClientRoleRepresentation(clientRepresentation, childClientRoleName);

        } catch (NotFoundException ex) {
            if (!createIfAbsent) {
                SynchronousDispatcher.rethrow(ex);
            }
            // create the new role as realmRole
            this.createRoleInClient(clientId, childRole);
            // Retrieve it back
            childClientRoleRepresentation = this.keycloakRoleResource.getClientRoleRepresentation(clientRepresentation, childClientRoleName);
        }

        RoleRepresentation mainClientRoleRepresentation = this.keycloakRoleResource.getClientRoleRepresentation(clientRepresentation, clientRoleName);
        mainClientRoleRepresentation.setComposite(Boolean.TRUE);

        List<RoleRepresentation> composites = new LinkedList<>();
        composites.add(childClientRoleRepresentation);

        this.keycloakRoleResource.getRolesById().addComposites(mainClientRoleRepresentation.getId(), composites);
    }

    /**
     *
     * @param clientId
     * @param clientRoleName
     * @throws java.io.IOException
     */
    @Override
    public void deleteClientRoleByClientId(String clientId, String clientRoleName)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(clientId, "ClientID cannot be empty.");
        Assert.requireNonEmpty(clientRoleName, "Client role name cannot be empty.");

        ClientRepresentation clientRepresentation = this.keycloakClientResource.getClientRepresentation(clientId);

        this.keycloakClientResource.getClientResource(clientRepresentation.getId()).roles().deleteRole(clientRoleName);
    }
}
