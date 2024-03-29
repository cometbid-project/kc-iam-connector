/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cometbid.integration.kc.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.cometbid.integration.kc.test.KeycloakProperties;
import org.keycloak.RSATokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.VerificationException;
import org.keycloak.jose.jws.JWSHeader;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class AbstractCredentialsFactory {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public AbstractCredentialsFactory(Keycloak keycloak, KeycloakProperties keycloakProperties) {
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
    }

    public Optional<AccessToken> getAccessToken() {
        return Optional.ofNullable(extractAccessTokenFrom(getAccessTokenString()));
    }

    public Optional<String> getAccessTokenString() {

        AccessTokenResponse tokenResponse = getAccessTokenResponse(keycloak);

        return Optional.ofNullable(tokenResponse == null ? null : tokenResponse.getToken());
    }

    private AccessTokenResponse getAccessTokenResponse(Keycloak keycloak) {
        try {
            return keycloak.tokenManager().getAccessToken();
        } catch (Exception ex) {
            log.error("Error occured", ex);
            return null;
        }
    }

    AccessToken extractAccessTokenFrom(Optional<String> optToken) {

        if (optToken.isEmpty()) {
            return null;
        }

        try {
            RSATokenVerifier verifier = RSATokenVerifier.create(optToken.get());
            Optional<PublicKey> publicKey = Optional.ofNullable(getRealmPublicKey(keycloak, verifier.getHeader()));

            if (publicKey.isPresent()) {
                return verifier.realmUrl(keycloakProperties.getRealmUrl()) //
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
            Map<String, Object> certInfos = om.readValue(new URL(keycloakProperties.getRealmCertsUrl())
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
