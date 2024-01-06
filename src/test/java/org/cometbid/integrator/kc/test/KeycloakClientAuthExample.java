/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.test;

import com.cometbid.integrator.kc.test.util.AbstractCredentialsFactory;
import org.cometbid.integrator.kc.iam.connector.realm.KeycloakClientFactory;
import java.util.Optional;
import org.cometbid.integrator.kc.iam.connector.config.KeycloakConfigProperties;
import static org.cometbid.integrator.kc.test.KeycloakProperties.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessToken;

/**
 *
 * @author samueladebowale
 */
public class KeycloakClientAuthExample {

    public static void main(String[] args) {

        KeycloakProperties keycloakProperties = new KeycloakProperties();
        
        //==========================================================================
        KeycloakConfigProperties configProp = new KeycloakConfigProperties(
                SERVER_URL, REALM_ID,
                CLIENT_ID, CLIENT_SECRET);

        KeycloakClientFactory clientFactory = new KeycloakClientFactory(configProp);
        //==========================================================================

        Keycloak keycloak = clientFactory.createClientCredentialKeycloak();
        AbstractCredentialsFactory credentialFactory = new AbstractCredentialsFactory(keycloak, keycloakProperties);

        // Get raw AccessToken string for client credentials grant
        System.out.println(credentialFactory.getAccessTokenString());

        // Get decoded AccessToken for client credentials grant
        // System.out.println(facade.getAccessToken());
        // Get decoded AccessToken for client credentials grant
        Optional<AccessToken> accessToken = credentialFactory.getAccessToken();
        if (accessToken.isPresent()) {
            System.out.println("Client credential grant: " + accessToken.get().getSubject());
        }

        //==========================================================================
        keycloak = clientFactory.createPasswordCredentialKeycloak("test", "test");
        credentialFactory = new AbstractCredentialsFactory(keycloak, keycloakProperties);

        // Get raw AccessToken string for client credentials grant
        System.out.println(credentialFactory.getAccessTokenString());

        // Get decoded AccessToken for client credentials grant
        // System.out.println(facade.getAccessToken());
        // Get decoded AccessToken for password credentials grant
        accessToken = credentialFactory.getAccessToken();
        if (accessToken.isPresent()) {
            System.out.println("Password grant: " + accessToken.get().getSubject());
        }
    }

}
