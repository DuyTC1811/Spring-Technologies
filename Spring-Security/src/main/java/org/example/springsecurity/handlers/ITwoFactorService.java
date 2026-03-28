package org.example.springsecurity.handlers;

public interface ITwoFactorService {
    boolean verifyCode(String secret, int code);

    byte[] generateQrCode(String secret, String username, String issuer);

    String buildOtpAuthUrl(String secret, String username, String issuer);

}
