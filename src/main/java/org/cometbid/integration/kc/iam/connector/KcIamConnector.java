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
package org.cometbid.integration.kc.iam.connector;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.cometbid.integration.kc.iam.connector.config.PasswordConfigProperties;

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
