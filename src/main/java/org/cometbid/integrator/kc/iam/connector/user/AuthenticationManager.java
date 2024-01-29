/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.keycloak.RSATokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.common.VerificationException;
import org.keycloak.jose.jws.JWSHeader;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.cometbid.integrator.kc.iam.connector.resource.KeycloakUserResource;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class AuthenticationManager implements AuthenticationManagerIT {

    private final KeycloakUserResource keycloakUserResource;

    public AuthenticationManager(KeycloakUserResource keycloakUserResource) {
        this.keycloakUserResource = keycloakUserResource;
    }

    /**
     *
     * @param username
     * @throws java.io.IOException
     */
    @Override
    public void logout(final String username) throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        userResource.logout();
    }

    /**
     *
     * @param username
     * @return
     * @throws java.io.IOException
     */
    @Override
    public Map<String, Object> impersonate(final String username) throws ClientErrorException, NotFoundException, IOException {

        UserResource userResource = this.keycloakUserResource.getUserResource(username);

        return userResource.impersonate();
    }

    /**
     * 
     * @return 
     */
    public Optional<AccessToken> login() {
        return Optional.ofNullable(extractAccessTokenFrom(getAccessTokenString()));
    }

    /**
     * 
     * @return 
     */
    public Optional<String> getAccessTokenString() {

        AccessTokenResponse tokenResponse = getAccessTokenResponse();

        return Optional.ofNullable(tokenResponse == null ? null : tokenResponse.getToken());
    }

    private AccessTokenResponse getAccessTokenResponse() {
        Keycloak keycloak = keycloakUserResource.realmResource().keycloak();

        try {
            return keycloak.tokenManager().getAccessToken();
        } catch (Exception ex) {
            log.error("Error occured", ex);
            return null;
        }
    }

    AccessToken extractAccessTokenFrom(Optional<String> optToken) {

        Keycloak keycloak = keycloakUserResource.realmResource().keycloak();

        if (optToken.isEmpty()) {
            return null;
        }

        try {
            RSATokenVerifier verifier = RSATokenVerifier.create(optToken.get());
            Optional<PublicKey> publicKey = Optional.ofNullable(getRealmPublicKey(keycloak, verifier.getHeader()));

            if (publicKey.isPresent()) {
                return verifier.realmUrl(keycloakUserResource.realmResource().getKeycloakProperties().getRealmUrl()) //
                        .publicKey(publicKey.get()) //
                        .verify() //
                        .getToken();
            }
        } catch (VerificationException e) {
            log.error("Verification error occured", e);
        }
        return null;
    }

    private PublicKey getRealmPublicKey(Keycloak keycloak, JWSHeader jwsHeader) {

        // Variant 1: use openid-connect /certs endpoint
        return retrievePublicKeyFromCertsEndpoint(jwsHeader);

        // Variant 2: use the Public Key referenced by the "kid" in the JWSHeader
        // in order to access realm public key we need at least realm role... e.g. view-realm
        //      return retrieveActivePublicKeyFromKeysEndpoint(keycloak, jwsHeader);
        // Variant 3: use the active RSA Public Key exported by the PublicRealmResource representation
        //      return retrieveActivePublicKeyFromPublicRealmEndpoint();
    }

    private PublicKey retrievePublicKeyFromCertsEndpoint(JWSHeader jwsHeader) {
        try {
            ObjectMapper om = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> certInfos = om.readValue(new URI(keycloakUserResource.realmResource()
                    .getKeycloakProperties().getRealmCertsUrl()).toURL()
                    .openStream(), Map.class);

            List<Map<String, Object>> keys = (List<Map<String, Object>>) certInfos.get("keys");

            Map<String, Object> keyInfo = null;
            for (Map<String, Object> key : keys) {
                String kid = (String) key.get("kid");

                if (jwsHeader.getKeyId().equals(kid)) {
                    keyInfo = key;
                    break;
                }
            }

            if (keyInfo == null) {
                return null;
            }

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            String modulusBase64 = (String) keyInfo.get("n");
            String exponentBase64 = (String) keyInfo.get("e");

            // see org.keycloak.jose.jwk.JWKBuilder#rs256
            Base64.Decoder urlDecoder = Base64.getUrlDecoder();
            BigInteger modulus = new BigInteger(1, urlDecoder.decode(modulusBase64));
            BigInteger publicExponent = new BigInteger(1, urlDecoder.decode(exponentBase64));

            return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));

        } catch (Exception e) {
            log.error("Error occured", e);
        }
        // return Optional.empty();;
        return null;
    }
}
