package org.example.spring2fa.config;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Component
public class TOTPAuthenticator {
    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final int TIME_STEP_SECONDS = 30;
    private static final int OTP_DIGITS = 6;
    private static final int OTP_MODULUS = 1_000_000;
    private static final int SECRET_SIZE = 10;
    private static final int QR_CODE_WIDTH = 250;
    private static final int QR_CODE_HEIGHT = 250;

    private final Base32 base32 = new Base32(true);
    private final SecureRandom secureRandom = new SecureRandom();

    public boolean verifyCode(String secret, int code, int variance) {
        validateSecret(secret);
        validateVariance(variance);

        long currentTimeIndex = System.currentTimeMillis() / 1000 / TIME_STEP_SECONDS;
        byte[] secretBytes = decodeSecret(secret);

        try {
            for (int offset = -variance; offset <= variance; offset++) {
                long expectedCode = generateCode(secretBytes, currentTimeIndex + offset);
                if (expectedCode == code) {
                    return true;
                }
            }
            return false;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to verify TOTP code", e);
        }
    }

    public String generateSecret() {
        byte[] secretBytes = new byte[SECRET_SIZE];
        secureRandom.nextBytes(secretBytes);
        return new String(base32.encode(secretBytes), StandardCharsets.UTF_8);
    }

    public String generateQrCodeBase64(String otpProtocol) {
        Objects.requireNonNull(otpProtocol, "otpProtocol must not be null");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(
                    otpProtocol,
                    BarcodeFormat.QR_CODE,
                    QR_CODE_WIDTH,
                    QR_CODE_HEIGHT
            );

            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (WriterException | IOException ex) {
            throw new IllegalStateException("Unable to generate QR code", ex);
        }
    }

    private long generateCode(byte[] secretBytes, long timeIndex) throws GeneralSecurityException {
        SecretKeySpec keySpec = new SecretKeySpec(secretBytes, HMAC_ALGORITHM);
        byte[] timeBytes = ByteBuffer.allocate(Long.BYTES).putLong(timeIndex).array();

        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(keySpec);
        byte[] hash = mac.doFinal(timeBytes);

        int offset = hash[hash.length - 1] & 0x0F;

        long truncatedHash = hash[offset] & 0x7F;
        for (int i = 1; i < 4; i++) {
            truncatedHash = (truncatedHash << 8) | (hash[offset + i] & 0xFF);
        }

        return truncatedHash % OTP_MODULUS;
    }

    private byte[] decodeSecret(String secret) {
        return base32.decode(secret);
    }

    private void validateSecret(String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("secret must not be null or blank");
        }
    }

    private void validateVariance(int variance) {
        if (variance < 0) {
            throw new IllegalArgumentException("variance must be greater than or equal to 0");
        }
    }
}
