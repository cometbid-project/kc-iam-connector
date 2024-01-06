/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
