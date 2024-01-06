/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.resource;

import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author samueladebowale
 */
public class KeycloakPasswordResource {

    private final PasswordEncoder passwordEncoder;

    public KeycloakPasswordResource(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public boolean isPasswordEqual(String plainTextPassword, String encodedPassword) {

        return passwordEncoder.matches(plainTextPassword, encodedPassword);
    }

    public boolean isPasswordUsedBefore(Set<String> pastUsedPasswords, String newPlainTextPassword) {

        return pastUsedPasswords.stream()
                .anyMatch(encodedPassword -> isPasswordEqual(newPlainTextPassword, encodedPassword));
    }

}
