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
import org.cometbid.integrator.kc.iam.connector.common.model.PagingModel;
import org.cometbid.integrator.kc.iam.connector.user.KeycloakUser;

/**
 *
 * @author samueladebowale
 */
public interface GroupFinderIT {

    KeycloakGroup findGroupById(String id) throws IOException;
    
    KeycloakGroup findGroupByPath(String path) throws IOException;

    List<KeycloakGroup> findAllPaginated(final PagingModel pageModel) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakGroup> searchWithResultPaginated(SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakGroup> searchMatchingWithResultPaginated(SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;

    Map<String, Long> countAll() throws IOException;

    Map<String, Long> countAllOccurences(SearchGroupCriteria searchGroupCriteria) throws IOException;

    Map<String, Long> countTopGroups(boolean onlyTopGroups) throws IOException;

    List<KeycloakGroup> subgroupsPaginated(String groupId, SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel) throws IOException;

    List<KeycloakGroup> subgroups(String groupId) throws IOException;

    Long countSubGroups(String groupId) throws IOException;

    List<String> associatedRealmRoles(String groupId) throws IOException;

    Map<String, List<String>> associatedClientRoles(String groupId) throws IOException;

    Map<String, List<String>> attributes(String groupId) throws IOException;

    Map<String, Boolean> access(String groupId) throws IOException;

    List<KeycloakUser> associatedMembers(String id, SearchGroupCriteria searchGroupCriteria, final PagingModel pageModel) throws IOException;

}
