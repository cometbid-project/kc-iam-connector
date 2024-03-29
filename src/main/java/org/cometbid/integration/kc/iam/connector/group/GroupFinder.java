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
package org.cometbid.integration.kc.iam.connector.group;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.cometbid.integration.kc.iam.connector.common.model.PagingModel;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakGroupResource;
import org.cometbid.integration.kc.iam.connector.user.KeycloakUser;
import org.cometbid.integration.kc.iam.connector.user.UserMapper;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class GroupFinder implements GroupFinderIT {

    private final KeycloakGroupResource keycloakGroupResource;
    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    public GroupFinder(KeycloakGroupResource keycloakGroupResource, GroupMapper groupMapper, UserMapper userMapper) {
        this.keycloakGroupResource = keycloakGroupResource;
        this.groupMapper = groupMapper;
        this.userMapper = userMapper;
    }

    /**
     *
     * @param groupId
     * @return
     * @throws IOException
     */
    @Override
    public KeycloakGroup findGroupById(String groupId) throws IOException {
        GroupRepresentation groupRepresentation = this.keycloakGroupResource.getGroupRepresentation(groupId);

        return groupMapper.toKeycloakGroup(groupRepresentation);
    }

    /**
     *
     * @param path
     * @return
     * @throws IOException
     */
    @Override
    public KeycloakGroup findGroupByPath(String path) throws IOException {

        GroupRepresentation groupRepresentation = this.keycloakGroupResource.findGroupByPath(path);

        return groupMapper.toKeycloakGroup(groupRepresentation);
    }

    /**
     *
     * @param pageModel
     * @return
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public List<KeycloakGroup> findAllPaginated(final PagingModel pageModel) throws ClientErrorException, NotFoundException, IOException {
        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        List<GroupRepresentation> groupRepresentations = this.keycloakGroupResource.getGroupsResource().groups(firstResult, pageSize);

        return groupRepresentations.stream().map(groupMapper::toKeycloakGroup).collect(Collectors.toList());
    }

    /**
     *
     * @param searchGroupCriteria
     * @param pageModel
     * @return
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public List<KeycloakGroup> searchWithResultPaginated(SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        List<GroupRepresentation> groupRepresentations = this.keycloakGroupResource.getGroupsResource()
                .groups(searchGroupCriteria.search(),
                        firstResult, pageSize, searchGroupCriteria.briefRepresentation());

        return groupRepresentations.stream().map(groupMapper::toKeycloakGroup).collect(Collectors.toList());
    }

    // search - search string for group
    // exact - exact match for search
    // first - index of the first element
    // max - max number of occurrences
    // briefRepresentation - if false, return groups with their attributes
    /**
     *
     *
     * @param searchGroupCriteria
     * @param pageModel
     * @return
     * @throws java.io.IOException
     */
    @Override
    public List<KeycloakGroup> searchMatchingWithResultPaginated(SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        List<GroupRepresentation> groupRepresentations = this.keycloakGroupResource.getGroupsResource().groups(
                searchGroupCriteria.search(), searchGroupCriteria.exactMatching(),
                firstResult, pageSize, searchGroupCriteria.briefRepresentation());

        return groupRepresentations.stream().map(groupMapper::toKeycloakGroup).collect(Collectors.toList());
    }

    /**
     *
     * @return @throws IOException
     */
    @Override
    public Map<String, Long> countAll() throws IOException {
        return this.keycloakGroupResource.getGroupsResource().count();
    }

    /**
     *
     * @param searchGroupCriteria
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, Long> countAllOccurences(SearchGroupCriteria searchGroupCriteria) throws IOException {
        return this.keycloakGroupResource.getGroupsResource().count(searchGroupCriteria.search());
    }

    /**
     *
     * @param onlyTopGroups
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, Long> countTopGroups(boolean onlyTopGroups) throws IOException {
        return this.keycloakGroupResource.getGroupsResource().count(onlyTopGroups);
    }

    /**
     *
     * @param groupId
     * @param searchGroupCriteria
     * @param pageModel
     * @return
     * @throws IOException
     */
    @Override
    public List<KeycloakGroup> subgroupsPaginated(String groupId, SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel) throws IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        List<GroupRepresentation> groupRepresentations = this.keycloakGroupResource.getGroupResource(groupId).getSubGroups(
                firstResult, pageSize, searchGroupCriteria.briefRepresentation());

        return groupRepresentations.stream().map(groupMapper::toKeycloakGroup).collect(Collectors.toList());
    }

    /**
     *
     * @param id
     * @param searchGroupCriteria
     * @param pageModel
     * @return
     * @throws IOException
     */
    @Override
    public List<KeycloakUser> associatedMembers(String id, SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel) throws IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        List<UserRepresentation> userRepresentations = this.keycloakGroupResource.getGroupResource(id)
                .members(firstResult, pageSize, searchGroupCriteria.briefRepresentation());

        return userRepresentations.stream().map(userMapper::toKeycloakUser).collect(Collectors.toList());
    }

    /**
     *
     * @param groupId
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, Boolean> access(String groupId) throws IOException {
        return this.keycloakGroupResource.getGroupRepresentation(groupId).getAccess();
    }

    /**
     *
     * @param groupId
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, List<String>> attributes(String groupId) throws IOException {
        return this.keycloakGroupResource.getGroupRepresentation(groupId).getAttributes();
    }

    /**
     *
     * @param groupId
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, List<String>> associatedClientRoles(String groupId) throws IOException {
        return this.keycloakGroupResource.getGroupRepresentation(groupId).getClientRoles();
    }

    /**
     *
     * @param groupId
     * @return
     * @throws IOException
     */
    @Override
    public List<String> associatedRealmRoles(String groupId) throws IOException {
        return this.keycloakGroupResource.getGroupRepresentation(groupId).getRealmRoles();
    }

    /**
     *
     * @param groupId
     * @return
     * @throws IOException
     */
    @Override
    public List<KeycloakGroup> subgroups(String groupId) throws IOException {

        List<GroupRepresentation> groupRepresentations = this.keycloakGroupResource.getGroupRepresentation(groupId).getSubGroups();

        return groupRepresentations.stream().map(groupMapper::toKeycloakGroup).collect(Collectors.toList());
    }

    /**
     *
     * @param groupId
     * @return
     * @throws IOException
     */
    @Override
    public Long countSubGroups(String groupId) throws IOException {
        return this.keycloakGroupResource.getGroupRepresentation(groupId).getSubGroupCount();
    }

}
