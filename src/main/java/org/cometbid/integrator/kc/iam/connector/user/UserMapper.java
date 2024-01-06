/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

import org.keycloak.representations.idm.SocialLinkRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author samueladebowale
 */
@Mapper//(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "mfaEnabled", source = "userRepresentation.totp")
    KeycloakUser toKeycloakUser(UserRepresentation userRepresentation);

    @Mapping(target = "totp", source = "keycloakUser.mfaEnabled")
    UserRepresentation toUserRepresentation(KeycloakUser keycloakUser);

    @Mapping(target = "socialUserId", source = "socialLink.providerUserId")
    @Mapping(target = "socialUsername", source = "socialLink.providerUserId")
    SocialLinkRepresentation toSocialLink(SocialLink socialLink);

    SocialLink toSocialLinkRepresentation(SocialLinkRepresentation socialLinkRepresentation);
}
