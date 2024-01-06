/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.totp;

/**
 *
 * @author samueladebowale
 */
public interface TotpManager {

    String generateSecret();

    boolean validateCode(String code, String secret);

    String generateQrImage(String email, String secret);

    String generateOtp();

    String[] generateRecoveryCodes();

}
