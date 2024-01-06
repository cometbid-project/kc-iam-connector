/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;

/**
 *
 * @author samueladebowale
 */
public record KeycloakClientScopeRepresentation(String id,
        String name,
        String description,
        String protocol,
        Map<String, String> attributes,
        List<ProtocolMapperRepresentation> protocolMappers) {

}
