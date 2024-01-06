/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import org.cometbid.integrator.kc.iam.connector.exception.ConflictException;
import org.keycloak.representations.idm.RealmRepresentation;

/**
 *
 * @author samueladebowale
 */
public sealed interface RealmManagerIT permits RealmManager {

    void createRealm(String realmName) throws ClientErrorException, IOException;

    void createRealm(RealmRepresentation customRealmRepresentation) throws ClientErrorException, IOException;

    void clearRealmKeysCache() throws ClientErrorException, NotFoundException, IOException;

    void clearRealmCache() throws ClientErrorException, NotFoundException, IOException;

    void clearRealmUserCache() throws ClientErrorException, NotFoundException, IOException;

    void makeDefaultGroup(String groupId) throws ConflictException, ClientErrorException, IOException;

    void deleteDefaultGroup(String groupId) throws ConflictException, ClientErrorException, IOException;

    void resetRealm(String realmName) throws NotFoundException, ClientErrorException, IOException;

    void logoutAll(String realmName) throws NotFoundException, ClientErrorException, IOException;
}
