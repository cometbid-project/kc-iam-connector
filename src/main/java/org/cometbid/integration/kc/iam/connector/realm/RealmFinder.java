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
package org.cometbid.integration.kc.iam.connector.realm;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.cometbid.integration.kc.iam.connector.group.GroupMapper;
import org.cometbid.integration.kc.iam.connector.group.KeycloakGroup;
import org.cometbid.integration.kc.iam.connector.realm.client.ClientScopeMapper;
import org.cometbid.integration.kc.iam.connector.realm.client.KeycloakClientScopeRepresentation;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakRealmResource;
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
