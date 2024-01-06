/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.role;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.Assert;
import org.cometbid.integrator.kc.iam.connector.common.model.PagingModel;
import org.cometbid.integrator.kc.iam.connector.resource.KeycloakClientResource;
import org.cometbid.integrator.kc.iam.connector.resource.KeycloakRoleResource;
import static org.cometbid.integrator.kc.iam.connector.util.Constants.ROLE_PREFIX;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.RoleRepresentation.Composites;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class RoleFinder implements RoleFinderIT {

    private final KeycloakRoleResource keycloakRoleResource;
    private final KeycloakClientResource keycloakClientResource;
    private final RoleMapper roleMapper;

    public RoleFinder(KeycloakRoleResource keycloakRoleResource,
            KeycloakClientResource keycloakClientResource, RoleMapper roleMapper) {
        this.keycloakRoleResource = keycloakRoleResource;
        this.keycloakClientResource = keycloakClientResource;
        this.roleMapper = roleMapper;
    }

    /**
     *
     * @param searchCriteria
     * @param pageModel
     * @return @throws java.io.IOException
     */
    @Override
    public List<KeycloakRole> searchRealm(SearchRoleCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException {

        boolean briefRepresentation = searchCriteria.briefRepresentation();

        List<RoleRepresentation> roleRepresentations = this.keycloakRoleResource.getRolesResource().list(briefRepresentation);

        return roleRepresentations.stream().map(roleMapper::toKeycloakRole).collect(Collectors.toList());
    }

    /**
     *
     * @param searchCriteria
     * @param pageModel
     * @return @throws java.io.IOException
     */
    @Override
    public List<KeycloakRole> searchRealmResultPaginated(SearchRoleCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        boolean briefRepresentation = searchCriteria.briefRepresentation();

        List<RoleRepresentation> roleRepresentations = this.keycloakRoleResource.getRolesResource()
                .list(firstResult, pageSize, briefRepresentation);

        return roleRepresentations.stream().map(roleMapper::toKeycloakRole).collect(Collectors.toList());
    }

    /**
     *
     * @param searchCriteria
     * @param pageModel
     * @return @throws java.io.IOException
     */
    @Override
    public List<KeycloakRole> searchRealmWithMaxOccurenceResultPaginated(SearchRoleCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        boolean briefRepresentation = searchCriteria.briefRepresentation();
        String maxOccurence = searchCriteria.search();

        List<RoleRepresentation> roleRepresentations = this.keycloakRoleResource.getRolesResource()
                .list(maxOccurence, firstResult, pageSize, briefRepresentation);

        return roleRepresentations.stream().map(roleMapper::toKeycloakRole).collect(Collectors.toList());
    }

    /**
     *
     * @return @throws java.io.IOException
     */
    @Override
    public List<KeycloakRole> findRealmRoles()
            throws ClientErrorException, NotFoundException, IOException {

        List<RoleRepresentation> roleRepresentations = this.keycloakRoleResource.getRolesResource().list();

        return roleRepresentations.stream().map(roleMapper::toKeycloakRole).collect(Collectors.toList());
    }

    /**
     *
     * @param clientId
     * @return
     * @throws java.io.IOException
     */
    @Override
    public List<KeycloakRole> findClientRolesByClientId(final String clientId)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(clientId, "ClientId must be specified.");

        ClientRepresentation clientRepresentation = this.keycloakClientResource.getClientRepresentation(clientId);

        ClientResource clientResource = this.keycloakClientResource.getClientResource(clientRepresentation.getId());

        List<RoleRepresentation> clientRoleRepresentations = clientResource.roles().list();

        return clientRoleRepresentations.stream().map(roleMapper::toKeycloakRole).collect(Collectors.toList());
    }

    /**
     *
     * @param uuid
     * @return
     * @throws java.io.IOException
     */
    @Override
    public List<KeycloakRole> findClientRolesById(final String uuid)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(uuid, "Client uuid must be specified.");

        ClientResource clientResource = this.keycloakClientResource.getClientResource(uuid);

        List<RoleRepresentation> clientRoleRepresentations = clientResource.roles().list();

        return clientRoleRepresentations.stream().map(roleMapper::toKeycloakRole).collect(Collectors.toList());
    }

    /**
     *
     * @param roleName
     * @return
     * @throws java.io.IOException
     */
    @Override
    public KeycloakRole findRealmRoleByName(final String roleName)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(roleName, "Role name cannot be empty.");

        final String upperRoleName = roleName.toUpperCase();
        final String realmRoleName = StringUtils.prependIfMissing(upperRoleName, ROLE_PREFIX);

        RoleRepresentation roleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(realmRoleName);

        return roleMapper.toKeycloakRole(roleRepresentation);
    }

    /**
     *
     * @param roleName
     * @param clientId
     * @return
     * @throws java.io.IOException
     */
    @Override
    public KeycloakRole findClientRoleByName(final String roleName, final String clientId)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(clientId, "ClientId must be specified.");
        Assert.requireNonEmpty(roleName, "Role name cannot be empty.");

        final String upperRoleName = roleName.toUpperCase();
        final String clientRoleName = StringUtils.prependIfMissing(upperRoleName, ROLE_PREFIX);

        ClientRepresentation clientRepresentation = this.keycloakClientResource.getClientRepresentation(clientId);

        RoleRepresentation roleRepresentation = this.keycloakRoleResource.getClientRoleRepresentation(clientRepresentation, clientRoleName);

        return roleMapper.toKeycloakRole(roleRepresentation);
    }

    /**
     *
     * @param roleName
     * @return @throws java.io.IOException
     */
    @Override
    public Map<String, List<String>> findRoleAttributes(String roleName)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(roleName, "Role name cannot be empty.");

        final String upperRoleName = roleName.toUpperCase();
        final String realmRoleName = StringUtils.prependIfMissing(upperRoleName, ROLE_PREFIX);

        RoleRepresentation realmRoleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(realmRoleName);
        return realmRoleRepresentation.getAttributes();
    }

    /**
     *
     * @param roleName
     * @return @throws java.io.IOException
     */
    @Override
    public KeycloakComposite findRoleComposites(String roleName)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(roleName, "Role name cannot be empty.");

        final String upperRoleName = roleName.toUpperCase();
        final String realmRoleName = StringUtils.prependIfMissing(upperRoleName, ROLE_PREFIX);

        RoleRepresentation realmRoleRepresentation = this.keycloakRoleResource.getRealmRoleRepresentation(realmRoleName);
        Composites composites = realmRoleRepresentation.getComposites();

        Set<String> realms = composites.getRealm();
        Map<String, List<String>> clients = composites.getClient();

        return KeycloakComposite.create(realms, clients);
    }
}
