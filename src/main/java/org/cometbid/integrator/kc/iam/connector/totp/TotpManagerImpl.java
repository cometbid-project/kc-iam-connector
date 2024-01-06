/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.integrator.kc.iam.connector.totp;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import static dev.samstevens.totp.util.Utils.getDataUriForImage;

import java.util.SplittableRandom;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.cometbid.integrator.kc.iam.connector.util.Constants;

/**
 *
 * @author samueladebowale
 */
@RequiredArgsConstructor
public class TotpManagerImpl implements TotpManager {

    // @Value("totp.code.length")
    private final String configOtpLength;
    // @Value("totp.qrCode.issuer")
    private final String configIssuer;
    private final CodeVerifier codeVerifier;
    private final QrDataFactory qrDataFactory;
    private final QrGenerator qrGenerator;
    private final SecretGenerator secretGenerator;

    /*
    @Value("totp.qrCode.issuer")
    private String ISSUER;
     */
    /**
     *
     * @return
     */
    @Override
    public String generateSecret() {
        return secretGenerator.generate();
    }

    /**
     *
     * @param code
     * @param secret
     * @return
     */
    @Override
    public boolean validateCode(String code, String secret) {
        return codeVerifier.isValidCode(secret, code);
    }

    /**
     * @param email
     * @param secret
     * @return
     * @Override
     */
    @Override
    public String generateQrImage(String email, String secret) throws RuntimeException {
        // Generate and store the secret
        // String secret = secretGenerator.generate();
        String qrCodeImage = null;
        try {
            String totpIssuer = Optional.ofNullable(configIssuer)
                    .filter(StringUtils::isNotBlank).orElse(Constants.ISSUER);

            QrData data = qrDataFactory.newBuilder().label(email).secret(secret).issuer(totpIssuer).build();

            byte[] imageData = qrGenerator.generate(data);
            String mimeType = qrGenerator.getImageMimeType();

            // Generate the QR code image data as a base64 string which
            // can be used in an <img> tag:
            qrCodeImage = getDataUriForImage(imageData, mimeType);
        } catch (QrGenerationException ex) {
        }

        return qrCodeImage;
    }

    /**
     *
     * @return
     */
    @Override
    public String generateOtp() {

        //Optional.ofNullable(configOtpLength).map(Ints::tryParse).orElse(Constants.OTP_LENGTH);
        int otpLength = Optional.ofNullable(configOtpLength)
                .filter(StringUtils::isNotBlank)
                .map(Integer::valueOf).orElse(Constants.OTP_LENGTH);

        SplittableRandom splittableRandom = new SplittableRandom();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < otpLength; ++i) {
            sb.append(splittableRandom.nextInt(0, 10));
        }

        return sb.toString();
    }

    /**
     *
     * @return
     */
    @Override
    public String[] generateRecoveryCodes() {
        RecoveryCodeGenerator recoveryCodes = new RecoveryCodeGenerator();
        String[] codes = recoveryCodes.generateCodes(16);
        return codes;
    }

}
