/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import org.cometbid.integrator.kc.iam.connector.group.*;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.cometbid.integrator.kc.iam.connector.resource.KeycloakGroupResource;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class ClientFinder implements ClientFinderIT {

    private final KeycloakGroupResource keycloakGroupResource;

    public ClientFinder(KeycloakGroupResource keycloakGroupResource) {
        this.keycloakGroupResource = keycloakGroupResource;
    }

    @Override
    public KeycloakGroup findClientById(String id) {

    }

    @Override
    public List<KeycloakGroup> findAllClient() {

    }

    @Override
    public List<KeycloakGroup> findAllClientIds() {

    }

}
