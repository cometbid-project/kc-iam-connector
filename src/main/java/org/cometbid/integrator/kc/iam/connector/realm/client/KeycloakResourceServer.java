/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.realm.client;

import java.util.List;
import org.keycloak.representations.idm.authorization.DecisionStrategy;
import org.keycloak.representations.idm.authorization.PolicyEnforcementMode;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;

/**
 *
 * @author samueladebowale
 */
public record KeycloakResourceServer(String id,
        String clientId,
        String name,
        boolean allowRemoteResourceManagement,
        PolicyEnforcementMode policyEnforcementMode,
        List<ResourceRepresentation> resources,
        List<PolicyRepresentation> policies,
        List<ScopeRepresentation> scopes,
        DecisionStrategy decisionStrategy) {

}
