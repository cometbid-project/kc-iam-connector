/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.config;

import java.util.Optional;
import org.cometbid.integrator.kc.iam.connector.util.Constants;

/**
 *
 * @author samueladebowale
 */
public record PasswordConfigProperties(Integer numberOfPastPassword, Boolean allowPasswordReuse) {

    public PasswordConfigProperties  {

        numberOfPastPassword = Optional.ofNullable(numberOfPastPassword)
                .orElseGet(() -> Constants.NUMBER_OF_PAST_PASSWORD);

        allowPasswordReuse = Optional.ofNullable(allowPasswordReuse)
                .orElseGet(() -> Constants.ALLOW_PASSWORD_REUSE);
    }

}
