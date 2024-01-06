/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.group;

import org.keycloak.representations.idm.GroupRepresentation;
import org.mapstruct.Mapper;

/**
 *
 * @author samueladebowale
 */
@Mapper//(componentModel = "spring")
public interface GroupMapper {
    
    KeycloakGroup toKeycloakGroup(GroupRepresentation groupRepresentation);

    GroupRepresentation toGroupRepresentation(KeycloakGroup keycloakGroup);
    
}
