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
package org.cometbid.integration.kc.iam.connector.totp;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
//import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import static dev.samstevens.totp.util.Utils.getDataUriForImage;

import java.util.SplittableRandom;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.cometbid.integration.kc.iam.connector.util.Constants;

/**
 *
 * @author samueladebowale
 */
public final class TotpManagerImpl implements TotpManager {

    private static TotpManagerImpl INSTANCE = new TotpManagerImpl();

    // @Value("totp.code.length")
    private final Integer configOtpLength;
    // @Value("totp.qrCode.issuer")
    private final String configIssuer;
    private final CodeVerifier codeVerifier;
    private final QrGenerator qrGenerator;
    private final SecretGenerator secretGenerator;

    private TotpManagerImpl() {
        this.configOtpLength = Constants.OTP_LENGTH;

        // this.configIssuer = Optional.ofNullable(configIssuer)
        //         .filter(StringUtils::isNotBlank).orElse(Constants.ISSUER);
        this.configIssuer = Constants.ISSUER;
        this.secretGenerator = new DefaultSecretGenerator();
        this.qrGenerator = new ZxingPngQrGenerator();
        this.codeVerifier = myCodeVerifier();
    }

    public static TotpManager getInstance() {
        return INSTANCE;
    }

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
           //String totpIssuer = Optional.ofNullable(configIssuer)
            //        .filter(StringUtils::isNotBlank).orElse(Constants.ISSUER);

            QrData data = new QrData.Builder()
                    .label(email)
                    .secret(secret)
                    .issuer(this.configIssuer)
                    .digits(6)
                    .period(30)
                    .algorithm(HashingAlgorithm.SHA512)
                    .build();

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

    private CodeVerifier myCodeVerifier() {
        // Time
        TimeProvider timeProvider = new SystemTimeProvider();

        // Code Generator
        CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA512, 6);
        DefaultCodeVerifier localCodeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        localCodeVerifier.setTimePeriod(30);
        localCodeVerifier.setAllowedTimePeriodDiscrepancy(2);
        return localCodeVerifier;
    }
}
