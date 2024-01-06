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
public record KeycloakUser(String id, String username, String password, String firstName,
        String lastName, ProfileStatus status, String email, boolean enabled, boolean emailVerified, boolean mfaEnabled,
        long createdTimestamp, String[] recoveryCodes, List<Role> roles, List<SocialLink> socialLinks) {

}
