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
package org.cometbid.integration.kc.iam.connector.config;

import lombok.Builder;

/**
 *
 * @author samueladebowale
 */
@Builder
public record KeycloakConfigProperties(String serverUrl, String realmName,
        String clientId, String clientSecret) {

    public String getRealmUrl() {
        return this.serverUrl + "/realms/" + this.realmName;
    }

    public String getRealmCertsUrl() {
        return getRealmUrl() + "/protocol/openid-connect/certs";
    }

    public static KeycloakConfigProperties create(String serverUrl, String realmName) {

        return KeycloakConfigProperties.builder()
                .serverUrl(serverUrl)
                .realmName(realmName)
                .build();
    }

    public static KeycloakConfigProperties create(String serverUrl, String realmName,
            String clientId, String clientSecret) {

        return KeycloakConfigProperties.builder()
                .serverUrl(serverUrl)
                .realmName(realmName)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
