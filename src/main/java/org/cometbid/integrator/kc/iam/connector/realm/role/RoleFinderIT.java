/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.role;

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
public interface RoleFinderIT {

    List<KeycloakRole> findRealmRoles()
            throws ClientErrorException, NotFoundException, IOException;
    
    List<KeycloakRole> searchRealmWithMaxOccurenceResultPaginated(SearchRoleCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;
    
    List<KeycloakRole> searchRealmResultPaginated(SearchRoleCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;
    
    List<KeycloakRole> searchRealm(SearchRoleCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException;

    KeycloakRole findRealmRoleByName(final String roleName)
            throws ClientErrorException, NotFoundException, IOException;

    KeycloakRole findClientRoleByName(final String roleName, final String clientId)
            throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakRole> findClientRolesById(final String uuid)
            throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakRole> findClientRolesByClientId(final String clientId)
            throws ClientErrorException, NotFoundException, IOException;

    Map<String, List<String>> findRoleAttributes(String roleName)
            throws ClientErrorException, NotFoundException, IOException;

    KeycloakComposite findRoleComposites(String roleName)
            throws ClientErrorException, NotFoundException, IOException;
}
