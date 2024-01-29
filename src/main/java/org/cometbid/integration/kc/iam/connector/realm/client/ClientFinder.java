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
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.cometbid.integration.kc.iam.connector.common.model.PagingModel;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;
import org.keycloak.representations.idm.authorization.ResourceServerRepresentation;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class ClientFinder implements ClientFinderIT {

    private final KeycloakClientResource keycloakClientResource;
    private final ClientMapper clientMapper;

    public ClientFinder(KeycloakClientResource keycloakClientResource, ClientMapper clientMapper) {
        this.keycloakClientResource = keycloakClientResource;
        this.clientMapper = clientMapper;
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public KeycloakClientRepresentation findClientByClientId(String clientId)
            throws NotFoundException, ClientErrorException, IOException {

        ClientRepresentation clientRepresentation = this.keycloakClientResource.getClientRepresentation(clientId);
        return this.clientMapper.toKeycloakClient(clientRepresentation);
    }

    /**
     *
     * @param id
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public KeycloakClientRepresentation findClientById(String id)
            throws NotFoundException, ClientErrorException, IOException {

        ClientRepresentation clientRepresentation = this.keycloakClientResource.getClientResource(id).toRepresentation();
        return this.clientMapper.toKeycloakClient(clientRepresentation);
    }

    /**
     *
     * @param viewableOnly
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public List<KeycloakClientRepresentation> findAllClient(boolean viewableOnly) throws NotFoundException, ClientErrorException, IOException {
        List<ClientRepresentation> clientRepresentations = this.keycloakClientResource.getClientsResource().findAll(viewableOnly);

        return clientRepresentations.stream().map(this.clientMapper::toKeycloakClient).collect(Collectors.toList());
    }

    /**
     *
     * @param clientId
     * @param viewableOnly
     * @param search
     * @param pageModel
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public List<KeycloakClientRepresentation> findAllClient(String clientId, boolean viewableOnly, boolean search, PagingModel pageModel)
            throws NotFoundException, ClientErrorException, IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        List<ClientRepresentation> clientRepresentations = this.keycloakClientResource.getClientsResource()
                .findAll(clientId, viewableOnly, search, firstResult, pageSize);

        return clientRepresentations.stream().map(this.clientMapper::toKeycloakClient).collect(Collectors.toList());
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public List<String> findDefaultClientScopes(String clientId)
            throws NotFoundException, ClientErrorException, IOException {

        return this.keycloakClientResource.getClientRepresentation(clientId).getDefaultClientScopes();
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public List<String> findOptionalClientScopes(String clientId)
            throws NotFoundException, ClientErrorException, IOException {

        return this.keycloakClientResource.getClientRepresentation(clientId).getOptionalClientScopes();
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public List<String> findRedirectUris(String clientId)
            throws NotFoundException, ClientErrorException, IOException {

        return this.keycloakClientResource.getClientRepresentation(clientId).getRedirectUris();
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public List<String> findWebOrigins(String clientId) throws NotFoundException, ClientErrorException, IOException {

        return this.keycloakClientResource.getClientRepresentation(clientId).getWebOrigins();
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public Map<String, Boolean> findAccess(String clientId) throws NotFoundException, ClientErrorException, IOException {

        return this.keycloakClientResource.getClientRepresentation(clientId).getAccess();
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public List<KeycloakProtocolMapper> findProtolMappers(String clientId)
            throws NotFoundException, ClientErrorException, IOException {

        List<ProtocolMapperRepresentation> protocolMappers = this.keycloakClientResource.getClientRepresentation(clientId).getProtocolMappers();

        return protocolMappers.stream().map(this.clientMapper::toKeycloakProtocolMapper).collect(Collectors.toList());
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public KeycloakResourceServer findAuthorizationSettings(String clientId)
            throws NotFoundException, ClientErrorException, IOException {

        ResourceServerRepresentation resourceServerRep = this.keycloakClientResource.getClientRepresentation(clientId).getAuthorizationSettings();

        return this.clientMapper.toKeycloakResourceServer(resourceServerRep);
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public Map<String, String> findAuthenticationFlowBindingOverrides(String clientId)
            throws NotFoundException, ClientErrorException, IOException {

        return this.keycloakClientResource.getClientRepresentation(clientId).getAuthenticationFlowBindingOverrides();
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public Map<String, String> findAttributes(String clientId) throws NotFoundException, ClientErrorException, IOException {

        return this.keycloakClientResource.getClientRepresentation(clientId).getAttributes();
    }

    /**
     *
     * @param clientId
     * @return
     * @throws NotFoundException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public Map<String, Integer> findRegisteredNodes(String clientId) throws NotFoundException, ClientErrorException, IOException {

        return this.keycloakClientResource.getClientRepresentation(clientId).getRegisteredNodes();
    }

}
