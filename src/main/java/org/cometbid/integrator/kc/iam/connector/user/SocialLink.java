/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;
import lombok.experimental.Accessors;
import org.cometbid.integrator.kc.iam.connector.enums.SocialProvider;

/**
 *
 * @author samueladebowale
 */
@Value
//@NoArgsConstructor
@Accessors(chain = true)
public class SocialLink {

    private String providerUserId;
    private SocialProvider socialProvider;

    /**
     *
     * @param providerUserId
     * @param socialProvider
     */
    @JsonCreator
    public SocialLink(String providerUserId, SocialProvider socialProvider) {
        // TODO Auto-generated constructor stub
        super();
        this.providerUserId = providerUserId;
        this.socialProvider = socialProvider;
    }

    /**
     *
     */
    public SocialLink() {
        this(null, null);
    }
}
