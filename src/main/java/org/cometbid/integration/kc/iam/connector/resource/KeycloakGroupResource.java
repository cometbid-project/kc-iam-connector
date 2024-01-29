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
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.representations.idm.GroupRepresentation;

/**
 *
 * @author samueladebowale
 */
public class KeycloakGroupResource {

    private final KeycloakRealmResource keycloakRealmResource;

    public KeycloakGroupResource(KeycloakRealmResource keycloakRealmResource) {
        this.keycloakRealmResource = keycloakRealmResource;
    }

    public KeycloakRealmResource realmResource() {
        return keycloakRealmResource;
    }

    /**
     *
     * @param groupId
     * @return
     * @throws java.io.IOException
     */
    public GroupRepresentation getGroupRepresentation(String groupId)
            throws ClientErrorException, NotFoundException, IOException {
        // Get realm group "tester" (requires view-realm role)

        GroupResource groupResource = getGroupResource(groupId);

        return groupResource.toRepresentation();
    }

    /**
     *
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public GroupsResource getGroupsResource()
            throws ClientErrorException, NotFoundException, IOException {

        return this.keycloakRealmResource.realmResource().groups();
    }

    /**
     *
     * @param groupId
     * @return @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public GroupResource getGroupResource(String groupId)
            throws ClientErrorException, NotFoundException, IOException {

        return this.getGroupsResource().group(groupId);
    }

    /**
     *
     * @param path
     * @return 
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    public GroupRepresentation findGroupByPath(String path)
            throws ClientErrorException, NotFoundException, IOException {
        
        return this.keycloakRealmResource.realmResource().getGroupByPath(path);
    }

}
