/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;
import org.keycloak.representations.idm.authorization.ResourceServerRepresentation;

/**
 *
 * @author samueladebowale
 */
public record KeycloakClientRepresentation(String id,
        String clientId,
        String name,
        String description,
        String rootUrl,
        String adminUrl,
        String baseUrl,
        Boolean surrogateAuthRequired,
        Boolean enabled,
        Boolean alwaysDisplayInConsole,
        String clientAuthenticatorType,
        String secret,
        String registrationAccessToken,
        Integer notBefore,
        Boolean bearerOnly,
        Boolean consentRequired,
        Boolean standardFlowEnabled,
        Boolean implicitFlowEnabled,
        Boolean directAccessGrantsEnabled,
        Boolean serviceAccountsEnabled,        
        Boolean authorizationServicesEnabled,
        Boolean publicClient,
        Boolean frontchannelLogout,
        String protocol,
        Map<String, String> attributes,
        Map<String, String> authenticationFlowBindingOverrides,
        Boolean fullScopeAllowed,
        Integer nodeReRegistrationTimeout,
        Map<String, Integer> registeredNodes,
        List<String> redirectUris,
        List<String> webOrigins,
        List<String> defaultClientScopes,
        List<String> optionalClientScopes,
        Map<String, Boolean> access,
        List<ProtocolMapperRepresentation> protocolMappers,
        ResourceServerRepresentation authorizationSettings,
        String origin) {

}
