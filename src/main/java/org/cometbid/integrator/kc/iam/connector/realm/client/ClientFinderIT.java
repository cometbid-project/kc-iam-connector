/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import org.cometbid.integrator.kc.iam.connector.group.*;
import java.util.List;

/**
 *
 * @author samueladebowale
 */
public interface ClientFinderIT {

    KeycloakGroup findClientById(final String id);

    List<KeycloakGroup> findAllClient();

    List<KeycloakGroup> findAllClientIds();
}
