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
package org.cometbid.integrator.kc.iam.connector.util;

/**
 *
 * @author samueladebowale
 */
public final class Constants {    

    private Constants() {
        // restrict instantiation
    }

    public final static String ROLE_PREFIX = "ROLE_";
    
    public static final String TOTP_SECRET = "totp_secret";
    public static final String TOTP_ENABLED = "2fa_flag";
    
    public static final String UPDATE_PASSWORD = "UPDATE_PASSWORD";

    public static final int OTP_LENGTH = 6;
    public static final String ISSUER = "Cometbid-SFI";
    
    public static final int NUMBER_OF_PAST_PASSWORD = 5;
    public static final boolean ALLOW_PASSWORD_REUSE = true;
    
    public static final String RECOVERY_CODES = "recovery_codes";
    
    public static final String GROUP_DESC = "DESCRIPTION";
}
