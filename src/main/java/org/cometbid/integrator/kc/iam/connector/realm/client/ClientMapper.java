/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import org.keycloak.representations.idm.ClientRepresentation;
import org.mapstruct.Mapper;

/**
 *
 * @author samueladebowale
 */
@Mapper//(componentModel = "spring")
public interface ClientMapper {

    KeycloakClientRepresentation toKeycloakGroup(ClientRepresentation clientRepresentation);

    ClientRepresentation toClientRepresentation(KeycloakClientRepresentation keycloakClient);
}
