/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integration.kc.test;

/**
 *
 * @author samueladebowale
 */
public class KeycloakProperties {

    static final String SERVER_URL = "http://localhost:8081/auth";
    static final String REALM_ID = "admin-client-demo";

    static final String CLIENT_ID = "demo-client-1";
    static final String CLIENT_SECRET = "da6947c2-6559-4c37-b219-d37bb72ec2fa";

    public String getRealmUrl() {
        return SERVER_URL + "/realms/" + REALM_ID;
    }

    public String getRealmCertsUrl() {
        return getRealmUrl() + "/protocol/openid-connect/certs";
    }
}
