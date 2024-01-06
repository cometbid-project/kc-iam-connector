/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

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
public sealed interface UserFinderIT permits UserFinder {

    List<KeycloakUser> searchUser(SearchUserCriteria searchCriteria, final PagingModel pageModel) throws IOException;

    KeycloakUser findById(String userId) throws ClientErrorException, NotFoundException, IOException;

    KeycloakUser findByUsername(String username) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByEmail(String email, final PagingModel pageModel) throws ClientErrorException, NotFoundException, IOException;

    KeycloakUser findByExactMatchingEmail(String email) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByFirstName(final String firstName) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByExactMatchingFirstName(final String firstName) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByLastName(final String lastName) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findByExactMatchingLastName(final String lastName) throws ClientErrorException, NotFoundException, IOException;

    List<KeycloakUser> findAll(final PagingModel pageModel) throws IOException;

    List<KeycloakUser> findByEmailVerificationStatus(boolean emailVerified, final PagingModel pageModel) throws IOException;
    
    List<String> findUserRealmRoles(final String username) throws ClientErrorException, NotFoundException, IOException;
    
    List<String> findUserGroups(final String username) throws ClientErrorException, NotFoundException, IOException;
    
    Map<String, List<String>> findUserClientRoles(final String username) throws ClientErrorException, NotFoundException, IOException;
}
