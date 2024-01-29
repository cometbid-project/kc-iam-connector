/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.cometbid.integrator.kc.iam.connector.group.GroupMapper;
import org.cometbid.integrator.kc.iam.connector.group.KeycloakGroup;
import org.cometbid.integrator.kc.iam.connector.realm.client.ClientScopeMapper;
import org.cometbid.integrator.kc.iam.connector.realm.client.KeycloakClientScopeRepresentation;
import org.cometbid.integrator.kc.iam.connector.resource.KeycloakRealmResource;
import org.keycloak.representations.idm.ClientScopeRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;

/**
 *
 * @author samueladebowale
 */
public class RealmFinder implements RealmFinderIT {

    private final KeycloakRealmResource keycloakRealmResource;
    private final ClientScopeMapper clientScopeMapper;
    private final GroupMapper groupMapper;

    public RealmFinder(KeycloakRealmResource keycloakRealmResource, GroupMapper groupMapper, ClientScopeMapper clientScopeMapper) {
        this.keycloakRealmResource = keycloakRealmResource;
        this.clientScopeMapper = clientScopeMapper;
        this.groupMapper = groupMapper;
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public List<KeycloakGroup> findDefaultGroups() throws ClientErrorException, NotFoundException, IOException {
        List<GroupRepresentation> list = this.keycloakRealmResource.realmResource().getDefaultGroups();

        return list.stream().map(groupMapper::toKeycloakGroup).collect(Collectors.toList());
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public List<KeycloakClientScopeRepresentation> findDefaultClientScopes() throws ClientErrorException, NotFoundException, IOException {
        List<ClientScopeRepresentation> list = this.keycloakRealmResource.realmResource().getDefaultDefaultClientScopes();

        return list.stream().map(clientScopeMapper::toKeycloakClientScope).collect(Collectors.toList());
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public List<KeycloakClientScopeRepresentation> findDefaultOptionalClientScopes() throws ClientErrorException, NotFoundException, IOException {
        List<ClientScopeRepresentation> list = this.keycloakRealmResource.realmResource().getDefaultOptionalClientScopes();

        return list.stream().map(clientScopeMapper::toKeycloakClientScope).collect(Collectors.toList());
    }

}
