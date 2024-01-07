/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;
import org.keycloak.representations.idm.authorization.ResourceServerRepresentation;
import org.mapstruct.Mapper;

/**
 *
 * @author samueladebowale
 */
@Mapper//(componentModel = "spring")
public interface ClientMapper {

    KeycloakClientRepresentation toKeycloakClient(ClientRepresentation clientRepresentation);

    ClientRepresentation toClientRepresentation(KeycloakClientRepresentation keycloakClient);

    KeycloakProtocolMapper toKeycloakProtocolMapper(ProtocolMapperRepresentation protocolMapperRepresentation);

    ProtocolMapperRepresentation toProtocolMapperRepresentation(KeycloakProtocolMapper keycloakProtocolMapper);

    KeycloakResourceServer toKeycloakResourceServer(ResourceServerRepresentation resourceServerRepresentation);

    ResourceServerRepresentation toResourceServerRepresentation(KeycloakResourceServer keycloakResourceServer);
}
