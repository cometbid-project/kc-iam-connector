/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
