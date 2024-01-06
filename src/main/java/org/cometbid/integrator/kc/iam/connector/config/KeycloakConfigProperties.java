/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.config;

/**
 *
 * @author samueladebowale
 */
public record KeycloakConfigProperties(String serverUrl, String realmName,
        String clientId, String clientSecret) {

    public String getRealmUrl() {
        return this.serverUrl + "/realms/" + this.realmName;
    }

    public String getRealmCertsUrl() {
        return getRealmUrl() + "/protocol/openid-connect/certs";
    }
}
