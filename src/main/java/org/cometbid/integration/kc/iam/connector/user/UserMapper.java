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
package org.cometbid.integration.kc.iam.connector.user;

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
