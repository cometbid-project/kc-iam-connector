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
package org.cometbid.integrator.kc.iam.connector.resource;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import static org.cometbid.integrator.kc.iam.connector.error.ErrorHandler.*;
import org.cometbid.integrator.kc.iam.connector.config.KeycloakConfigProperties;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.representations.AccessToken;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class KeycloakRealmResource {

    private final KeycloakConfigProperties keycloakProperties;
    private final Keycloak keycloak;

    public KeycloakRealmResource(Keycloak keycloak, KeycloakConfigProperties keycloakProperties) {
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
    }

    public KeycloakConfigProperties getKeycloakProperties() {
        return this.keycloakProperties;
    }

    public Keycloak keycloak() {
        return this.keycloak;
    }

    /**
     *
     * @return @throws NotFoundException
     * @throws java.io.IOException
     */
    public RealmResource realmResource()
            throws NotFoundException, ClientErrorException, IOException {
        // Get realm role "tester" (requires view-realm role)     

        String realmName = keycloakProperties.realmName();

        RealmResource realmResource = null;
        try {
            realmResource = this.keycloak.realm(realmName);

            log.debug("{} was found.", realmName);
        } catch (NotFoundException e) {
            String errorMessage = """
                                  Realm '%s' was not found.
                                  """.formatted(realmName);

            handleException(e, errorMessage);
        } catch (Exception e) {
            String errorMessage = """
                                  Error occured readin realm '%s': error-message %s.
                                  """.formatted(realmName, e.getMessage());

            handleException(e, errorMessage);
        }

        return realmResource;
    }

    /**
     *
     * @return access-token as <code>AccessToken</code>
     * @throws ClientErrorException Failed with an error code due to server
     * error
     * @throws JWSInputException Failed to parse access-token string
     * @throws IOException Failed to connect with Keycloak server
     */
    public AccessToken getAccessToken() throws ClientErrorException, JWSInputException, IOException {

        AccessToken accessToken = null;
        try {
            String accessTokenString = this.keycloak.tokenManager().getAccessTokenString();
            log.debug("accessTokenString: {}", accessTokenString);

            JWSInput input = new JWSInput(accessTokenString);
            accessToken = input.readJsonContent(AccessToken.class);

            log.debug("subject: {}", accessToken.getSubject());
            log.debug("preferredUsername: {}", accessToken.getPreferredUsername());
            log.debug("givenName: {}", accessToken.getGivenName());

        } catch (ClientErrorException e) {
            handleClientErrorException(e);
        } catch (JWSInputException e) {
            log.error("Failed to parse access-token string", e);

            SynchronousDispatcher.rethrow(e);
        }

        return accessToken;
    }

}
