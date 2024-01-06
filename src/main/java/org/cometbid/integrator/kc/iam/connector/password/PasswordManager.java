/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.password;

import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class PasswordManager {

    /*
    private PasswordEncoder passwordEncoder;

    public PasswordManager(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isPasswordEqual(String plainTextPassword, String encodedPassword) {

        return passwordEncoder.matches(plainTextPassword, encodedPassword);
    }

    public boolean isPasswordUsedBefore(Set<String> pastUsedPasswords, String newPlainTextPassword) {

        return pastUsedPasswords.stream()
                .anyMatch(hashedPassword -> isPasswordEqual(newPlainTextPassword, hashedPassword));
    }
     */
}
