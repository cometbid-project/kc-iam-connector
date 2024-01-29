/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.cometbid.integrator.kc.iam.connector.common.model.PagingModel;

/**
 *
 * @author samueladebowale
 */
public interface ClientFinderIT {

    KeycloakClientRepresentation findClientByClientId(final String id) throws NotFoundException, ClientErrorException, IOException;

    KeycloakClientRepresentation findClientById(String id) throws NotFoundException, ClientErrorException, IOException;

    List<KeycloakClientRepresentation> findAllClient(boolean viewableOnly) throws NotFoundException, ClientErrorException, IOException;

    List<KeycloakClientRepresentation> findAllClient(String clientId, boolean viewableOnly, boolean search, PagingModel pageModel)
            throws NotFoundException, ClientErrorException, IOException;

    List<String> findDefaultClientScopes(String clientId) throws NotFoundException, ClientErrorException, IOException;

    List<String> findOptionalClientScopes(String clientId) throws NotFoundException, ClientErrorException, IOException;

    List<String> findRedirectUris(String clientId) throws NotFoundException, ClientErrorException, IOException;

    List<String> findWebOrigins(String clientId) throws NotFoundException, ClientErrorException, IOException;

    Map<String, Boolean> findAccess(String clientId) throws NotFoundException, ClientErrorException, IOException;

    List<KeycloakProtocolMapper> findProtolMappers(String clientId) throws NotFoundException, ClientErrorException, IOException;

    KeycloakResourceServer findAuthorizationSettings(String clientId) throws NotFoundException, ClientErrorException, IOException;

    Map<String, String> findAuthenticationFlowBindingOverrides(String clientId) throws NotFoundException, ClientErrorException, IOException;

    Map<String, String> findAttributes(String clientId) throws NotFoundException, ClientErrorException, IOException;

    Map<String, Integer> findRegisteredNodes(String clientId) throws NotFoundException, ClientErrorException, IOException;
}
