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
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.cometbid.integration.kc.iam.connector.exception.ConflictException;

/**
 *
 * @author samueladebowale
 */
public sealed interface RoleManagerIT permits RoleManager {

    void createRoleInRealm(Role role)
            throws ConflictException, NotFoundException, ClientErrorException, IOException;

    void createRoleInClient(String clientId, Role role)
            throws ConflictException, NotFoundException, ClientErrorException, IOException;

    void addAttribute(String roleName, RoleAttribute roleAttr)
            throws ClientErrorException, NotFoundException, IOException;

    void setAttributes(String roleName, Map<String, List<String>> attributes)
            throws ClientErrorException, NotFoundException, IOException;

    void updateRole(Role role)
            throws ClientErrorException, NotFoundException, IOException;

    void makeRealmRoleComposite(final String roleToMakeComposite, final Role childRole, boolean createIfAbsent)
            throws ClientErrorException, NotFoundException, IOException;

    void makeClientRoleComposite(final String roleToMakeComposite, final String clientId,
            final Role childRole, boolean createIfAbsent)
            throws ClientErrorException, NotFoundException, IOException;

    void makeRealmRoleCompositeWithClientRole(final String roleToMakeComposite, final Role childRole,
            final String clientId, boolean createIfAbsent)
            throws ClientErrorException, NotFoundException, IOException;

    void deleteRealmRole(String realmRoleName)
            throws ClientErrorException, NotFoundException, IOException;

    void deleteClientRole(String uuid, String roleName)
            throws ClientErrorException, NotFoundException, IOException;

    void deleteClientRoleByClientId(String clientId, String clientRoleName)
            throws ClientErrorException, NotFoundException, IOException;
}
