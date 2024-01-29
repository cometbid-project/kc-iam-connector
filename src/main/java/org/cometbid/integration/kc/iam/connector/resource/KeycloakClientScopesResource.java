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
package org.cometbid.integration.kc.iam.connector.resource;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import org.keycloak.admin.client.resource.ClientScopeResource;
import org.keycloak.admin.client.resource.ClientScopesResource;
import org.keycloak.representations.idm.ClientScopeRepresentation;

/**
 *
 * @author samueladebowale
 */
public class KeycloakClientScopesResource {

    private final KeycloakRealmResource keycloakRealmResource;

    public KeycloakClientScopesResource(KeycloakRealmResource keycloakRealmResource) {
        this.keycloakRealmResource = keycloakRealmResource;
    }

    /**
     *
     * @param uuid
     * @return
     * @throws java.io.IOException
     */
    public ClientScopeRepresentation getClientRepresentation(String uuid) 
                        throws NotFoundException, ClientErrorException, IOException {

        // Get client
        ClientScopeResource clientScopeResource = getClientScopeResource(uuid);
        //
        return clientScopeResource.toRepresentation();
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public ClientScopesResource getClientScopesResource() 
                        throws ClientErrorException, NotFoundException, IOException {

        return this.keycloakRealmResource.realmResource().clientScopes();
    }

    /**
     *
     * @param uuid clientScope UUID 
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public ClientScopeResource getClientScopeResource(String uuid) 
                            throws ClientErrorException, NotFoundException, IOException {

        return this.getClientScopesResource().get(uuid);
    }
}
