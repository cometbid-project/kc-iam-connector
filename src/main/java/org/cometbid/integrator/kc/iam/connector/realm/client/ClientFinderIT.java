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
