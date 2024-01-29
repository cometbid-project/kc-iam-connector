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
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.Assert;
import static org.cometbid.integrator.kc.iam.connector.error.ErrorHandler.handleException;
import static org.cometbid.integrator.kc.iam.connector.error.ErrorHandler.throwConflictException;
import static org.cometbid.integrator.kc.iam.connector.error.ErrorHandler.throwUnexpectedStatusException;
import org.cometbid.integrator.kc.iam.connector.exception.ConflictException;
import org.cometbid.integrator.kc.iam.connector.resource.KeycloakGroupResource;
import static org.cometbid.integrator.kc.iam.connector.util.Constants.GROUP_DESC;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.representations.idm.GroupRepresentation;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public final class GroupManager implements GroupManagerIT {

    private final KeycloakGroupResource keycloakGroupResource;

    public GroupManager(KeycloakGroupResource keycloakGroupResource) {
        this.keycloakGroupResource = keycloakGroupResource;
    }

    /**
     *
     * @param group
     * @return
     * @throws IOException
     */
    @Override
    public Group createGroup(final Group group) throws IOException {

        Assert.requireNonEmpty(group.name(), "Group name cannot be empty.");
        //Assert.requireNonEmpty(group.description(), "Group description cannot be empty.");

        String groupName = group.name().toUpperCase();
        String groupDesc = group.description();

        try {
            GroupRepresentation realmGroup = new GroupRepresentation();
            realmGroup.setName(groupName);
            realmGroup.setSubGroupCount(Long.valueOf(0));
            realmGroup.setParentId(null);
            realmGroup.singleAttribute(GROUP_DESC,
                    StringUtils.isBlank(groupDesc) ? "Group: " + groupName : groupDesc);
            realmGroup.setRealmRoles(Collections.emptyList());
            realmGroup.setClientRoles(Collections.emptyMap());
            realmGroup.setSubGroups(Collections.emptyList());
            realmGroup.setAccess(Collections.emptyMap());

            String path = """
                      /%s
                      """.formatted(groupName);
            realmGroup.setPath(path);

            log.info("Realm group {}", realmGroup);
            Response response = this.keycloakGroupResource.getGroupsResource().add(realmGroup);

            log.info("Response: {} {}", response.getStatus(), response.getStatusInfo());

            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                log.debug("Group '{}' was created.", groupName);

                String id = this.getCreatedGroupId(response);

                return Group.createGroup(id, groupName, groupDesc);

            } else if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                throwConflictException("""
                                      Group '%s' has already been created.
                                       """.formatted(groupName));
            } else {

                throwUnexpectedStatusException(
                        "Unexpected response status", response.getStatus());
            }
        } catch (Exception e) {
            String errorMessage
                    = """
                    Failed to create keycloak realm user: %s""".formatted(e.getMessage());

            handleException(e, errorMessage);
        }

        return null;
    }

    private String getCreatedGroupId(Response response) {
        // URI location = response.getLocation();
        log.info("Response: {}-{}", response.getStatus(), response.getStatusInfo());
        log.info("Resource location {}", response.getLocation());

        return CreatedResponseUtil.getCreatedId(response);
    }

    /**
     *
     * @param parentGroup
     * @param childGroup
     * @param createIfAbsent
     * @throws java.io.IOException
     */
    @Override
    public void makeGroupComposite(final Group parentGroup, final Group childGroup, boolean createIfAbsent)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(parentGroup.id(), "Realm Grroup id to make composite cannot be empty.");
        Assert.requireNonEmpty(childGroup.id(), "Realm Group id to add as child cannot be empty.");

        if (parentGroup.name().equalsIgnoreCase(childGroup.name())) {
            throwConflictException("""
                                      A realm group: '%s' cannot have same name '%s' as child group
                                       """.formatted(parentGroup.name(), childGroup.name()));
        }

        //final String groupName = parentGroup.name().toUpperCase();
        //final String childGroupName = childGroup.name().toUpperCase();
        GroupRepresentation childGroupRepresentation = null;

        try {
            childGroupRepresentation = this.keycloakGroupResource.getGroupRepresentation(childGroup.id());

            GroupResource groupResource = this.keycloakGroupResource.getGroupResource(parentGroup.id());
            groupResource.subGroup(childGroupRepresentation);

            GroupRepresentation groupRepresentation = groupResource.toRepresentation();
            Long subGroupCount = groupRepresentation.getSubGroupCount() + 1;
            groupRepresentation.setSubGroupCount(subGroupCount);

            groupResource.update(groupRepresentation);

        } catch (IOException ex) {
            log.error("Failed to make group composite", ex);

            SynchronousDispatcher.rethrow(ex);
        }
    }

    /**
     *
     * @param group
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void updateGroup(Group group)
            throws ClientErrorException, NotFoundException, IOException {

        Assert.requireNonEmpty(group.name(), "Group name cannot be empty.");
        Assert.requireNonEmpty(group.id(), "Group id cannot be empty.");

        String groupId = group.id();
        String groupName = group.name();
        String groupDesc = group.description();

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);
        groupRepresentation.setName(group.name());
        groupRepresentation.singleAttribute(GROUP_DESC,
                StringUtils.isBlank(groupDesc) ? "Group: " + groupName : groupDesc);

        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param groupAttr
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void addAttribute(String groupId, GroupAttribute groupAttr)
            throws ClientErrorException, NotFoundException, IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);
        groupRepresentation.singleAttribute(groupAttr.keyname(), groupAttr.value());

        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param attributes
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void setAttributes(String groupId, Map<String, List<String>> attributes)
            throws ClientErrorException, NotFoundException, IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);
        groupRepresentation.setAttributes(attributes);

        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param realmRoles
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void setRealmRoles(String groupId, List<String> realmRoles)
            throws ClientErrorException, NotFoundException, IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);
        groupRepresentation.setRealmRoles(realmRoles);

        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param realmRole
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void addRealmRole(String groupId, String realmRole)
            throws ClientErrorException, NotFoundException, IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);
        List<String> realmRoles = groupRepresentation.getRealmRoles();

        realmRoles.add(realmRole);

        // groupRepresentation.setRealmRoles(realmRoles);
        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param accesses
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void setAccess(String groupId, Map<String, Boolean> accesses)
            throws ClientErrorException, NotFoundException, IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);
        groupRepresentation.setAccess(accesses);

        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param accessName
     * @param turnOn
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void addAccess(String groupId, String accessName, Boolean turnOn)
            throws ClientErrorException, NotFoundException, IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);
        Map<String, Boolean> accesses = groupRepresentation.getAccess();
        accesses.put(accessName, turnOn);

        //groupRepresentation.setAccess(accesses);
        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param clientRoles
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void setClientRoles(String groupId, Map<String, List<String>> clientRoles)
            throws ClientErrorException, NotFoundException, IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);
        groupRepresentation.setClientRoles(clientRoles);

        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param clientRoleMapping
     * @param overrideExisting
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void addClientRole(String groupId, ClientRoles clientRoleMapping, boolean overrideExisting)
            throws ClientErrorException, NotFoundException, IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);
        Map<String, List<String>> clientRoles = groupRepresentation.getClientRoles();

        if (overrideExisting) {
            clientRoles.put(clientRoleMapping.clientId(), clientRoleMapping.clientRoles());
        } else {
            clientRoles.putIfAbsent(clientRoleMapping.clientId(), clientRoleMapping.clientRoles());
        }

        // groupRepresentation.setClientRoles(clientRoles);
        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param subGroups
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void setSubGroup(String groupId, List<String> subGroups)
            throws ClientErrorException, NotFoundException, IOException {

        if (CollectionUtils.isEmpty(subGroups)) {
            return;
        }

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);

        List<GroupRepresentation> subGrouplist = new ArrayList<>();

        for (String subgroupId : subGroups) {
            GroupRepresentation subGroupRep = this.keycloakGroupResource.getGroupRepresentation(subgroupId);

            subGrouplist.add(subGroupRep);
        }

        groupRepresentation.setSubGroups(subGrouplist);
        groupRepresentation.setSubGroupCount(Long.valueOf(subGrouplist.size()));

        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param groupId
     * @param subgroupId
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public void addSubGroup(String groupId, String subgroupId)
            throws ClientErrorException, NotFoundException, IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);

        GroupRepresentation subGroupRep = this.keycloakGroupResource.getGroupRepresentation(subgroupId);
        List<GroupRepresentation> subGrouplist = groupRepresentation.getSubGroups();
        subGrouplist.add(subGroupRep);

        groupRepresentation.setSubGroups(subGrouplist);
        groupRepresentation.setSubGroupCount(Long.valueOf(subGrouplist.size()));

        this.keycloakGroupResource.getGroupResource(groupId).update(groupRepresentation);
    }

    /**
     *
     * @param intoGroupId
     * @param fromGroupId
     * @param deleteAfter
     * @throws ConflictException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public void merge(String intoGroupId, String fromGroupId, boolean deleteAfter)
            throws ConflictException, ClientErrorException, IOException {

        GroupRepresentation destGroup = this.keycloakGroupResource.getGroupRepresentation(intoGroupId);

        GroupRepresentation sourceGroup = this.keycloakGroupResource.getGroupRepresentation(fromGroupId);

        destGroup.merge(sourceGroup);

        if (deleteAfter) {
            this.keycloakGroupResource.getGroupResource(fromGroupId).remove();
        }

        this.keycloakGroupResource.getGroupResource(intoGroupId).update(destGroup);
    }

    /**
     *
     * @param groupId
     * @throws ConflictException
     * @throws ClientErrorException
     * @throws IOException
     */
    @Override
    public void delete(String groupId) throws ConflictException, ClientErrorException, IOException {
        this.keycloakGroupResource.getGroupResource(groupId).remove();
    }

}
