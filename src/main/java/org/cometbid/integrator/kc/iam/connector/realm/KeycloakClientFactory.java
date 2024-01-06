/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm;

import org.cometbid.integrator.kc.iam.connector.config.KeycloakConfigProperties;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

/**
 *
 * @author samueladebowale
 */
public class KeycloakClientFactory {

    private final KeycloakConfigProperties keycloakProperties;

    public KeycloakClientFactory(KeycloakConfigProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    private KeycloakBuilder newKeycloakBuilderWithPasswordCredentials(String username, String password) {
        return newKeycloakBuilderWithClientCredentials() //
                .username(username) //
                .password(password) //
                .grantType(OAuth2Constants.PASSWORD);
    }

    private KeycloakBuilder newKeycloakBuilderWithClientCredentials() {
        return KeycloakBuilder.builder() //
                .realm(keycloakProperties.realmName()) //
                .serverUrl(keycloakProperties.serverUrl())//
                .clientId(keycloakProperties.clientId()) //
                .clientSecret(keycloakProperties.clientSecret()) //
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS);
    }

    public Keycloak createClientCredentialKeycloak() {
        return newKeycloakBuilderWithClientCredentials().build();
    }

    public Keycloak createPasswordCredentialKeycloak(String username, String password) {
        return newKeycloakBuilderWithPasswordCredentials(username, password).build();
    }

}
