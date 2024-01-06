/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author samueladebowale
 */
public record KeycloakComposite(Set<String> realms, Map<String, List<String>> clients) {

    static KeycloakComposite create(Set<String> realms, Map<String, List<String>> clients) {
        return new KeycloakComposite(realms, clients);
    }
    
}
