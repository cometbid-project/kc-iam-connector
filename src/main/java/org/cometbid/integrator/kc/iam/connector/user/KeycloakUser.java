/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserConsentRepresentation;
import org.keycloak.representations.idm.UserProfileMetadata;

/**
 *
 * @author samueladebowale
 */
public record KeycloakUser(String id, String self, String username, String firstName,
        String lastName, String email, boolean enabled, boolean emailVerified, boolean mfaEnabled,
        long createdTimestamp, 
        String origin,
        String federationLink, 
        String serviceAccountClientId, 
        List<SocialLink> socialLinks,
        List<String> groups,
        Map<String, Boolean> access,
        UserProfileMetadata userProfileMetadata,
        Map<String, List<String>> attributes,
        List<CredentialRepresentation> credentials,
        Set<String> disableableCredentialTypes,
        List<String> requiredActions,
        List<FederatedIdentityRepresentation> federatedIdentities,
        List<String> realmRoles,
        Map<String, List<String>> clientRoles,
        List<UserConsentRepresentation> clientConsents,
        Integer notBefore) {
}
