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
import org.cometbid.integrator.kc.iam.connector.exception.ConflictException;

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
