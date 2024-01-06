/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package org.cometbid.integrator.kc.iam.connector;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.cometbid.integrator.kc.iam.connector.config.PasswordConfigProperties;

/**
 *
 * @author samueladebowale
 */
public class KcIamConnector {

    /*
     * 
     * 
        **Checkout my one-line code answer:**

        https://stackoverflow.com/a/77739302/4621922
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");

        String nullable = "";
        Integer result = Optional.ofNullable("").filter(StringUtils::isNotBlank).map(Integer::valueOf).orElse(0);
        System.out.println("Result1: " + result);

        nullable = null;
        result = Optional.ofNullable(nullable).filter(StringUtils::isNotBlank).map(Integer::valueOf).orElse(0);
        System.out.println("Result2: " + result);

        nullable = "6";
        result = Optional.ofNullable(nullable).filter(StringUtils::isNotBlank).map(Integer::valueOf).orElse(0);
        System.out.println("Result3: " + result);

        nullable = "a";
        result = Optional.ofNullable(nullable).filter(StringUtils::isNotBlank)
                .filter(NumberUtils::isCreatable).map(Integer::valueOf).orElse(0);
        System.out.println("Result4: " + result);

        PasswordConfigProperties configProp = new PasswordConfigProperties(null, null);
        System.out.println("Result5: " + configProp.toString());

        configProp = new PasswordConfigProperties(5, null);
        System.out.println("Result6: " + configProp.toString());
        
        configProp = new PasswordConfigProperties(null, Boolean.FALSE);
        System.out.println("Result7: " + configProp.toString());
        
        configProp = new PasswordConfigProperties(5, Boolean.FALSE);
        System.out.println("Result8: " + configProp.toString());
    }
}
