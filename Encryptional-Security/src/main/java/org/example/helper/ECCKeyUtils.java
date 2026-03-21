package org.example.helper;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

public final class ECCKeyUtils {
    private static final String EC = "EC";
    private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
    private static final String BEGIN_ECC_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    private static final String END_ECC_PRIVATE_KEY = "-----END PRIVATE KEY-----";
    private static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERTIFICATE = "-----END CERTIFICATE-----";

    public static PrivateKey loadPrivateKeyECCFromPem(String pem) {
        if (pem != null && !pem.contains(BEGIN_ECC_PRIVATE_KEY)) {
            throw new IllegalArgumentException(
                    "PKCS#1 private key is not supported directly. Convert it to PKCS#8: -----BEGIN EC PRIVATE KEY-----");
        }

        validatePemBlock(pem, BEGIN_ECC_PRIVATE_KEY, END_ECC_PRIVATE_KEY, "EC private key");
        byte[] der = decodePemBlock(pem, BEGIN_ECC_PRIVATE_KEY, END_ECC_PRIVATE_KEY);
        return loadPrivateKeyFromDer(der);
    }

    private static void validatePemBlock(String pem, String beginMarker, String endMarker, String objectName) {
        if (pem == null || pem.isBlank()) {
            throw new IllegalArgumentException(objectName + " PEM content must not be null or blank");
        }
        if (!pem.contains(beginMarker) || !pem.contains(endMarker)) {
            throw new IllegalArgumentException("Invalid " + objectName + " PEM format");
        }
    }

    private static byte[] decodePemBlock(String pem, String beginMarker, String endMarker) {
        String normalized = pem
                .replace(beginMarker, "")
                .replace(endMarker, "")
                .replaceAll("\\s", "");

        try {
            return Base64.getDecoder().decode(normalized);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("PEM content is not valid Base64", exception);
        }
    }


    public static PrivateKey loadPrivateKeyFromDer(byte[] derBytes) {
        Objects.requireNonNull(derBytes, "derBytes must not be null");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(EC);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(derBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(spec);

            if (!(privateKey instanceof ECPrivateKey)) {
                throw new IllegalArgumentException("Provided private key is not an RSA private key");
            }
            return privateKey;
        } catch (GeneralSecurityException exception) {
            throw new IllegalArgumentException("Invalid RSA private key content", exception);
        }
    }

    public static PublicKey loadPublicKeyFromDer(byte[] derBytes) {
        Objects.requireNonNull(derBytes, "derBytes must not be null");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(EC);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(derBytes);
            PublicKey publicKey = keyFactory.generatePublic(spec);

            if (!(publicKey instanceof RSAPublicKey)) {
                throw new IllegalArgumentException("Provided public key is not an RSA public key");
            }
            return publicKey;
        } catch (GeneralSecurityException exception) {
            throw new IllegalArgumentException("Invalid RSA public key content", exception);
        }
    }

    public static X509Certificate loadCertificateFromPem(String pem) {
        validatePemBlock(pem, BEGIN_CERTIFICATE, END_CERTIFICATE, "X.509 certificate");
        byte[] der = decodePemBlock(pem, BEGIN_CERTIFICATE, END_CERTIFICATE);
        return loadCertificate(der);
    }

    public static X509Certificate loadCertificate(byte[] certificateBytes) {
        Objects.requireNonNull(certificateBytes, "certificateBytes must not be null");
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(
                    new java.io.ByteArrayInputStream(certificateBytes)
            );
            if (!(certificate instanceof X509Certificate x509Certificate)) {
                throw new IllegalArgumentException("Provided certificate is not X.509");
            }
            return x509Certificate;
        } catch (GeneralSecurityException exception) {
            throw new IllegalArgumentException("Invalid X.509 certificate content", exception);
        }
    }
}
