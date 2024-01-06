/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

import jakarta.validation.constraints.Size;

/**
 *
 * @author samueladebowale
 */
public record SearchUserCriteria(@Size(max = 50, message = "{Username.size}") String username, 
        @Size(max = 50, message = "{FirstName.size}") String firstName,
        @Size(max = 50, message = "{LastName.size}") String lastName, 
        @Size(max = 50, message = "{Email.size}") String email, 
        @Size(max = 50, message = "{Role.name.size}") String roleName, 
        boolean emailVerified, boolean profileEnabled,
        boolean briefRepresentation, boolean exactMatching) {

}
