/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.group;

import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.GroupRepresentation;

/**
 *
 * @author samueladebowale
 */
public record KeycloakGroup(String id,
        String name,
        String path,
        String parentId,
        Long subGroupCount,
        Map<String, List<String>> attributes,
        List<GroupRepresentation> subGroups,
        List<String> realmRoles,
        Map<String, List<String>> clientRoles,
        Map<String, Boolean> access) {

}
