/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
