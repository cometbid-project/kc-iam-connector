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
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.cometbid.integrator.kc.iam.connector.common.model.PagingModel;
import org.cometbid.integrator.kc.iam.connector.resource.KeycloakUserResource;
import org.keycloak.representations.idm.UserRepresentation;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public final class UserFinder implements UserFinderIT {

    private final KeycloakUserResource keycloakUserResource;
    private final UserMapper userMapper;

    public UserFinder(KeycloakUserResource keycloakUserResource,
            UserMapper userMapper) {
        this.keycloakUserResource = keycloakUserResource;
        this.userMapper = userMapper;
    }

    /**
     *
     * @param searchCriteria
     * @param pageModel
     * @return
     * @throws java.io.IOException
     */
    @Override
    public List<KeycloakUser> searchUser(SearchUserCriteria searchCriteria, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        boolean userEnabled = searchCriteria.profileEnabled();
        boolean briefRepresentation = searchCriteria.briefRepresentation();

        String username = searchCriteria.username();
        String firstName = searchCriteria.firstName();
        String lastName = searchCriteria.lastName();
        String email = searchCriteria.email();
        boolean emailVerified = searchCriteria.emailVerified();

        List<UserRepresentation> userRepresentations = this.keycloakUserResource.getUsersResource()
                .search(username, firstName, lastName, email,
                        emailVerified, firstResult, pageSize,
                        userEnabled, briefRepresentation);

        return userRepresentations.stream().map(userMapper::toKeycloakUser).collect(Collectors.toList());
    }

    /**
     *
     * @param userId
     * @return
     * @throws IOException
     */
    @Override
    public KeycloakUser findById(String userId) throws IOException {

        UserRepresentation userRepresentation = this.keycloakUserResource.getUserResourceById(userId)
                .toRepresentation();

        return userMapper.toKeycloakUser(userRepresentation);
    }

    /**
     *
     * @param username
     * @return
     * @throws IOException
     */
    @Override
    public KeycloakUser findByUsername(String username) throws ClientErrorException, NotFoundException, IOException {

        UserRepresentation userRepresentation = this.keycloakUserResource.getUserRepresentation(username);

        return userMapper.toKeycloakUser(userRepresentation);
    }

    /**
     *
     * @param email
     * @return
     * @throws IOException
     */
    @Override
    public KeycloakUser findByExactMatchingEmail(String email) throws NotFoundException, IOException {

        boolean exactMatching = true;
        List<UserRepresentation> userRepresentationList = this.keycloakUserResource.getUsersResource()
                .searchByEmail(email, exactMatching);

        if (userRepresentationList.isEmpty()) {
            String errorMessage = """
                                  User with '%s' was not found.
                                  """.formatted(email);

            throw new NotFoundException(errorMessage);
        }

        UserRepresentation userRepresentation = userRepresentationList.get(0);

        return userMapper.toKeycloakUser(userRepresentation);
    }

    /**
     *
     * @param email
     * @param pageModel
     * @return
     * @throws IOException
     */
    @Override
    public List<KeycloakUser> findByEmail(String email, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException {

        int firstResult = pageModel.getPageNo();
        int maxResult = pageModel.getPageSize();

        // Create the user resource
        List<UserRepresentation> userRepresentations = this.keycloakUserResource.getUsersResource()
                .search(email, firstResult, maxResult);

        return userRepresentations.stream().map(userMapper::toKeycloakUser).collect(Collectors.toList());
    }

    /**
     *
     * @param emailVerified
     * @param pageModel
     * @return
     * @throws IOException
     */
    @Override
    public List<KeycloakUser> findByEmailVerificationStatus(boolean emailVerified, final PagingModel pageModel)
            throws ClientErrorException, NotFoundException, IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        boolean userEnabled = true;
        boolean briefRepresentation = true;

        List<UserRepresentation> userRepresentations = this.keycloakUserResource.getUsersResource()
                .search(emailVerified, firstResult, pageSize,
                        userEnabled, briefRepresentation);

        return userRepresentations.stream().map(userMapper::toKeycloakUser).collect(Collectors.toList());
    }

    /**
     *
     * @param pageModel
     * @return
     * @throws IOException
     */
    @Override
    public List<KeycloakUser> findAll(final PagingModel pageModel) throws ClientErrorException, NotFoundException, IOException {

        int pageNo = pageModel.getPageNo();
        int pageSize = pageModel.getPageSize();
        int firstResult = pageNo * pageSize;

        List<UserRepresentation> userRepresentations = this.keycloakUserResource.getUsersResource().list(firstResult, pageSize);

        return userRepresentations.stream().map(userMapper::toKeycloakUser).collect(Collectors.toList());
    }

    /**
     *
     * @param firstName
     * @return
     * @throws java.io.IOException
     */
    @Override
    public List<KeycloakUser> findByFirstName(final String firstName) throws ClientErrorException, NotFoundException, IOException {

        boolean exactMatching = false;

        // Create the user resource
        List<UserRepresentation> userRepresentations = this.keycloakUserResource.getUsersResource().searchByFirstName(firstName, exactMatching);

        return userRepresentations.stream().map(userMapper::toKeycloakUser).collect(Collectors.toList());
    }

    /**
     *
     * @param firstName
     * @return
     * @throws java.io.IOException
     */
    @Override
    public List<KeycloakUser> findByExactMatchingFirstName(final String firstName) throws ClientErrorException, NotFoundException, IOException {

        boolean exactMatching = true;

        // Create the user resource
        List<UserRepresentation> userRepresentations = this.keycloakUserResource.getUsersResource().searchByFirstName(firstName, exactMatching);

        return userRepresentations.stream().map(userMapper::toKeycloakUser).collect(Collectors.toList());
    }

    /**
     *
     * @param lastName
     * @return
     * @throws java.io.IOException
     */
    @Override
    public List<KeycloakUser> findByLastName(final String lastName) throws ClientErrorException, NotFoundException, IOException {

        boolean exactMatching = false;
        // Create the user resource
        List<UserRepresentation> userRepresentations = this.keycloakUserResource.getUsersResource().searchByLastName(lastName, exactMatching);

        return userRepresentations.stream().map(userMapper::toKeycloakUser).collect(Collectors.toList());
    }

    /**
     *
     * @param lastName
     * @return
     * @throws java.io.IOException
     */
    @Override
    public List<KeycloakUser> findByExactMatchingLastName(final String lastName) throws ClientErrorException, NotFoundException, IOException {

        boolean exactMatching = true;
        // Create the user resource
        List<UserRepresentation> userRepresentations = this.keycloakUserResource.getUsersResource().searchByLastName(lastName, exactMatching);

        return userRepresentations.stream().map(userMapper::toKeycloakUser).collect(Collectors.toList());
    }

    /**
     *
     * @param username
     * @return
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public List<String> findUserRealmRoles(final String username) throws ClientErrorException, NotFoundException, IOException {

        UserRepresentation userRepresentation = this.keycloakUserResource.getUserRepresentation(username);

        return userRepresentation.getRealmRoles();
    }
    
    /**
     *
     * @param username
     * @return
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException
     */
    @Override
    public List<String> findUserGroups(final String username) throws ClientErrorException, NotFoundException, IOException {

        UserRepresentation userRepresentation = this.keycloakUserResource.getUserRepresentation(username);

        return userRepresentation.getGroups();
    }

    /**
     * 
     * @param username
     * @return
     * @throws ClientErrorException
     * @throws NotFoundException
     * @throws IOException 
     */
    @Override
    public Map<String, List<String>> findUserClientRoles(final String username) throws ClientErrorException, NotFoundException, IOException {

        UserRepresentation userRepresentation = this.keycloakUserResource.getUserRepresentation(username);

        return userRepresentation.getClientRoles();
    }

}
