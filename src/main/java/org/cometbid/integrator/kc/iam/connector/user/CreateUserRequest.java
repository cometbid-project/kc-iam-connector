/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

import java.util.List;
import org.cometbid.integrator.kc.iam.connector.enums.ProfileStatus;
import org.cometbid.integrator.kc.iam.connector.realm.role.Role;

/**
 *
 * @author samueladebowale
 */
public record CreateUserRequest(KeycloakUser keycloakUser, ProfileStatus profileStatus,
        List<Role> roles, String plainPassword, List<String> recoveryCodelist) {

}
