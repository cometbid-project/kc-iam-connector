/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.List;
import org.cometbid.integrator.kc.iam.connector.group.KeycloakGroup;
import org.cometbid.integrator.kc.iam.connector.realm.client.KeycloakClientScopeRepresentation;

/**
 *
 * @author samueladebowale
 */
public interface RealmFinderIT {
    
    List<KeycloakGroup> findDefaultGroups() throws ClientErrorException, NotFoundException, IOException;
    
    List<KeycloakClientScopeRepresentation> findDefaultClientScopes() throws ClientErrorException, NotFoundException, IOException;
    
    List<KeycloakClientScopeRepresentation> findDefaultOptionalClientScopes() throws ClientErrorException, NotFoundException, IOException;
}
