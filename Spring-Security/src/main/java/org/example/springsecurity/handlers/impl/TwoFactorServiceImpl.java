package org.example.springsecurity.handlers.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.codec.binary.Base32;
import org.example.springsecurity.exceptions.BaseException;
import org.example.springsecurity.handlers.ITwoFactorService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.example.springsecurity.enums.EException.OTP_INVALID;
import static org.example.springsecurity.utils.TOTPUtil.genCode;

@Service
public class TwoFactorServiceImpl implements ITwoFactorService {

    @Override
    public boolean verifyCode(String secret, int code) {
        // variance = 0 → chỉ check đúng chu kỳ hiện tại (30 giây)
        // variance = 1 → check 3 chu kỳ (90 giây)
        int variance = 1;
            try {
                long timeIndex = System.currentTimeMillis() / 1000 / 30;
                byte[] secretBytes = new Base32().decode(secret);

                for (int i = -variance; i <= variance; i++) {
                    if (genCode(secretBytes, timeIndex + i) == code) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                throw new BaseException(OTP_INVALID);
            }

    }

    @Override
    public byte[] generateQrCode(String secret, String username, String issuer) {
        try {
            String otpAuth = String.format(
                    "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                    URLEncoder.encode(issuer, StandardCharsets.UTF_8),
                    URLEncoder.encode(username, StandardCharsets.UTF_8),
                    secret,
                    URLEncoder.encode(issuer, StandardCharsets.UTF_8)
            );

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    otpAuth,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("Generate QR Code failed", e);
        }
    }

    @Override
    public String buildOtpAuthUrl(String secret, String username, String issuer) {
        return "";
    }
}
