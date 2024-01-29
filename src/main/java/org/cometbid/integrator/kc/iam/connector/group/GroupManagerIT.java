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
package org.cometbid.integrator.kc.iam.connector.group;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.cometbid.integrator.kc.iam.connector.exception.ConflictException;

/**
 *
 * @author samueladebowale
 */
public sealed interface GroupManagerIT permits GroupManager {

    Group createGroup(final Group group) throws ClientErrorException, NotFoundException, IOException;

    void addAttribute(String groupId, GroupAttribute groupAttr) throws ClientErrorException, NotFoundException, IOException;

    void setAttributes(String groupId, Map<String, List<String>> attributes) throws ClientErrorException, NotFoundException, IOException;

    void setSubGroup(String groupId, List<String> subGroups) throws ClientErrorException, NotFoundException, IOException;

    void addSubGroup(String groupId, String subgroupId) throws ClientErrorException, NotFoundException, IOException;

    void setRealmRoles(String groupId, List<String> realmRoles) throws ClientErrorException, NotFoundException, IOException;

    void addRealmRole(String groupId, String realmRole) throws ClientErrorException, NotFoundException, IOException;

    void setClientRoles(String groupId, Map<String, List<String>> clientRoles) throws ClientErrorException, NotFoundException, IOException;

    void addClientRole(String groupId, ClientRoles clientRoleMapping, boolean overrideExisting) throws ClientErrorException, NotFoundException, IOException;

    void setAccess(String groupId, Map<String, Boolean> accesses) throws ClientErrorException, NotFoundException, IOException;

    void addAccess(String groupId, String accessName, Boolean turnOn) throws ClientErrorException, NotFoundException, IOException;

    void makeGroupComposite(final Group parentGroup, final Group childGroup, boolean createIfAbsent) throws ClientErrorException, NotFoundException, IOException;

    void updateGroup(Group group) throws ClientErrorException, NotFoundException, IOException;

    void merge(String intoGroupId, String fromGroupId, boolean deleteAfter) throws ConflictException, ClientErrorException, IOException;

    void delete(String groupId) throws ConflictException, ClientErrorException, IOException;
}
