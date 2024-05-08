/*
 * The MIT License
 *
 * Copyright 2024 Cometbid.Org.
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
package org.cometbid.integration.kc.test.it;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import static org.assertj.core.api.Assertions.assertThat;
import org.cometbid.integration.kc.iam.connector.config.KeycloakConfigProperties;
import org.cometbid.integration.kc.iam.connector.config.PasswordConfigProperties;
import org.cometbid.integration.kc.iam.connector.enums.ProfileStatus;
import org.cometbid.integration.kc.iam.connector.realm.role.Role;
import org.cometbid.integration.kc.iam.connector.resource.KeycloakRealmResource;
import org.cometbid.integration.kc.iam.connector.totp.TotpManagerImpl;
import org.cometbid.integration.kc.iam.connector.user.CreateUserRequest;
import org.cometbid.integration.kc.iam.connector.user.CreateUserResponse;
import org.cometbid.integration.kc.iam.connector.user.KeycloakUser;
import org.cometbid.integration.kc.iam.connector.user.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class KeycloakIntegration {

    private Keycloak keycloakAdmin;
    private UserManager userService;

    private String tokenEndpointUrl;
    private String logoutEndpointUrl;
    private String userInfoEndpointUrl;
    private String revokeTokenEndpointUrl;

    private static String myRealm = "test-realm";

    @Container
    protected static final KeycloakContainer keycloakContainer = new KeycloakContainer()
            .withRealmImportFile("keycloak/realm-export.json")
            //.useTls()
            //.useTls("your_custom.crt", "your_custom.key")
            .withFeaturesEnabled("docker", "impersonation",
                    "scripts", "token-exchange",
                    "admin-fine-grained-authz")
            .withFeaturesDisabled("authorization");

    @SuppressWarnings("unused")
    @DynamicPropertySource
    static void jwtValidationProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/" + myRealm);
        
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/" + myRealm + "/protocol/openid-connect/certs");
    }

    @BeforeEach
    void setup() {

        tokenEndpointUrl = keycloakContainer.getAuthServerUrl() + "realms/myrealm/protocol/openid-connect/token";
        logoutEndpointUrl = keycloakContainer.getAuthServerUrl() + "realms/myrealm/protocol/openid-connect/logout";
        userInfoEndpointUrl = keycloakContainer.getAuthServerUrl() + "realms/myrealm/protocol/openid-connect/userinfo";
        revokeTokenEndpointUrl = keycloakContainer.getAuthServerUrl() + "realms/myrealm/protocol/openid-connect/revoke";

        log.info("Token endpoint Url {}", tokenEndpointUrl);

        keycloakAdmin = keycloakContainer.getKeycloakAdminClient();
        KeycloakConfigProperties keycloakConfigProperties
                = KeycloakConfigProperties.create(keycloakContainer.getAuthServerUrl(), myRealm);

        KeycloakRealmResource keycloakRealmResource = new KeycloakRealmResource(keycloakAdmin, keycloakConfigProperties);

        PasswordConfigProperties configProp = PasswordConfigProperties.create(10, Boolean.TRUE);

        userService = UserManager.builder()
                .keycloakRealmResource(keycloakRealmResource)
                .totpManager(TotpManagerImpl.getInstance())
                .configProp(configProp)
                .build();

    }

    /**
     *
     */
    @DisplayName("to test create user works")
    @Test
    public void testThatCreateUserWorks() throws Exception {
        // correct email
        String[] rolesArray = {"ROLE_ADMIN"};
        String username = "janedoe";
        String firstName = "Jane";
        String lastName = "Doe";
        String emailAddr = "jane.doe@gmail.com";

        KeycloakUser keycloakUser = KeycloakUser.create(username, firstName, lastName, emailAddr);
        ProfileStatus profileStatus = ProfileStatus.PROFILE_ACTIVE;
        String plainPassword = "default-password";
        List<String> recoveryCodelist = null;

        List<String> roles = Arrays.asList(rolesArray);
        Set<String> myRoles = new HashSet<>(roles);

        CreateUserRequest userRequest = CreateUserRequest.createRequest(keycloakUser, profileStatus, myRoles, plainPassword, recoveryCodelist);

        CreateUserResponse response = userService.createUser(userRequest);

        assertThat(response != null);
    }

}
