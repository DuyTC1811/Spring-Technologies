package org.example.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;

public final class HelperKeyUtils {
    public static final String RSA = "RSA";
    public static final String EC = "EC";
    private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
    private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    private static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
    private static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERTIFICATE = "-----END CERTIFICATE-----";

    public static PublicKey loadPublicKey(Path path, String algorithm) {
        Objects.requireNonNull(path, "path must not be null");
        try {
            byte[] content = Files.readAllBytes(path);
            return isPem(content)
                    ? loadPublicKeyFromPem(new String(content, StandardCharsets.UTF_8), algorithm)
                    : loadPublicKeyFromDer(content, algorithm);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot read public key file: " + path, exception);
        }
    }

    public static PrivateKey loadPrivateKeyFromPem(String pem, String algorithm) {
        if (pem != null && !pem.contains(BEGIN_PRIVATE_KEY)) {
            throw new IllegalArgumentException(
                    "PKCS#1 private key is not supported directly. Convert it to PKCS#8: -----BEGIN PRIVATE KEY-----");
        }

        validatePemBlock(pem, BEGIN_PRIVATE_KEY, END_PRIVATE_KEY, "EC private key");
        byte[] der = decodePemBlock(pem, BEGIN_PRIVATE_KEY, END_PRIVATE_KEY);
        return loadPrivateKeyFromDer(der, algorithm);
    }

    public static PrivateKey loadPrivateKeyFromDer(byte[] derBytes, String algorithm) {
        Objects.requireNonNull(derBytes, "derBytes must not be null");
        Objects.requireNonNull(algorithm, "algorithm must not be null");

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(derBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(spec);

            if (RSA.equals(algorithm) && !(privateKey instanceof RSAPrivateKey)) {
                throw new IllegalArgumentException("Provided private key is not an RSA private key");
            }

            if (EC.equals(algorithm) && !(privateKey instanceof ECPrivateKey)) {
                throw new IllegalArgumentException("Provided private key is not an EC private key");
            }

            return privateKey;
        } catch (GeneralSecurityException exception) {
            throw new IllegalArgumentException("Invalid " + algorithm + " private key content", exception);
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

    public static String loadResourceAsString(String path) {
        String normalized = path.startsWith("/") ? path.substring(1) : path;

        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(normalized)) {
            if (is == null) {
                throw new RuntimeException("File not found in classpath: " + normalized);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Load resource failed: " + normalized, e);
        }
    }

    private static boolean isPem(byte[] content) {
        if (content == null || content.length == 0) {
            return false;
        }
        String text = new String(content, StandardCharsets.UTF_8);
        return text.contains("-----BEGIN ");
    }


    // PUBLIC API - LOAD FROM PEM STRING
    public static PublicKey loadPublicKeyFromPem(String pem, String algorithm) {
        validatePemBlock(pem, BEGIN_PUBLIC_KEY, END_PUBLIC_KEY, "Public key");
        byte[] der = decodePemBlock(pem, BEGIN_PUBLIC_KEY, END_PUBLIC_KEY);
        return loadPublicKeyFromDer(der, algorithm);
    }

    // PUBLIC API - LOAD FROM DER BYTES
    public static PublicKey loadPublicKeyFromDer(byte[] derBytes, String algorithm) {
        Objects.requireNonNull(derBytes, "derBytes must not be null");

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(derBytes);

            PublicKey publicKey = keyFactory.generatePublic(spec);

            if (RSA.equals(algorithm) && !(publicKey instanceof RSAPublicKey)) {
                throw new IllegalArgumentException("Not an RSA public key");
            }

            if (EC.equals(algorithm) && !(publicKey instanceof ECPublicKey)) {
                throw new IllegalArgumentException("Not an EC public key");
            }

            return publicKey;

        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Invalid " + algorithm + " public key", e);
        }
    }

    public static String signature(String data, PrivateKey privateKey) {
        Objects.requireNonNull(data, "data must not be null");
        Objects.requireNonNull(privateKey, "privateKey must not be null");

        try {
            String algorithm = resolveSignAlgorithm(privateKey.getAlgorithm());

            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] signBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signBytes);

        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Sign error using " + privateKey.getAlgorithm(), e);
        }
    }

    public static boolean verify(String data, String signatureStr, PublicKey publicKey) {
        Objects.requireNonNull(data, "data must not be null");
        Objects.requireNonNull(signatureStr, "signatureStr must not be null");
        Objects.requireNonNull(publicKey, "publicKey must not be null");

        try {
            Signature signature = Signature.getInstance(resolveSignAlgorithm(publicKey.getAlgorithm()));
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(signatureStr));
        } catch (GeneralSecurityException exception) {
            throw new IllegalArgumentException(
                    "Cannot verify signature with key algorithm: " + publicKey.getAlgorithm(),
                    exception
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Signature is not valid Base64", exception);
        }
    }

    private static String resolveSignAlgorithm(String keyAlgorithm) {
        if (keyAlgorithm == null || keyAlgorithm.isBlank()) {
            throw new IllegalArgumentException("Key algorithm must not be null or blank");
        }

        return switch (keyAlgorithm.toUpperCase(Locale.ROOT)) {
            case "RSA" -> "SHA256withRSA";
            case "EC", "ECDSA" -> "SHA256withECDSA";
            default -> throw new IllegalArgumentException("Unsupported key algorithm: " + keyAlgorithm);
        };
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
}
