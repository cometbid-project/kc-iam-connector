/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.role;

import org.keycloak.representations.idm.RoleRepresentation;
import org.mapstruct.Mapper;

/**
 *
 * @author samueladebowale
 */
@Mapper//(componentModel = "spring")
public interface RoleMapper {

    KeycloakRole toKeycloakRole(RoleRepresentation roleRepresentation);

    RoleRepresentation toRoleRepresentation(KeycloakRole keycloakRole);
}
