/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.role;

import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.RoleRepresentation;

/**
 *
 * @author samueladebowale
 */
public record KeycloakRole(String id,
        String name,
        String description,
        boolean composite,
        Boolean clientRole,
        String containerId,
        Map<String, List<String>> attributes,
        RoleRepresentation.Composites composites) {
}
