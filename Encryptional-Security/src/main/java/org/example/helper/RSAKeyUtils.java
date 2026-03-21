package org.example.helper;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
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

public final class RSAKeyUtils {

    private static final String RSA = "RSA";
    private static final String EC = "EC";
    private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
    private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    private static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
    private static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERTIFICATE = "-----END CERTIFICATE-----";

    private RSAKeyUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // =========================
    // Public API - load from file
    // =========================

    public static PublicKey loadPublicKey(Path path) {
        Objects.requireNonNull(path, "path must not be null");
        try {
            byte[] content = Files.readAllBytes(path);
            return isPem(content)
                    ? loadPublicKeyFromPem(new String(content, StandardCharsets.UTF_8))
                    : loadPublicKeyFromDer(content);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot read public key file: " + path, exception);
        }
    }

    public static PrivateKey loadPrivateKey(Path path) {
        Objects.requireNonNull(path, "path must not be null");
        try {
            byte[] content = Files.readAllBytes(path);
            return isPem(content)
                    ? loadPrivateKeyFromPem(new String(content, StandardCharsets.UTF_8))
                    : loadPrivateKeyFromDer(content);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot read private key file: " + path, exception);
        }
    }

    public static X509Certificate loadCertificate(Path path) {
        Objects.requireNonNull(path, "path must not be null");
        try {
            byte[] content = Files.readAllBytes(path);
            return loadCertificate(content);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Cannot read certificate file: " + path, exception);
        }
    }

    // =========================
    // Public API - load from PEM string
    // =========================

    public static PublicKey loadPublicKeyFromPem(String pem) {
        validatePemBlock(pem, BEGIN_PUBLIC_KEY, END_PUBLIC_KEY, "RSA public key");
        byte[] der = decodePemBlock(pem, BEGIN_PUBLIC_KEY, END_PUBLIC_KEY);
        return loadPublicKeyFromDer(der);
    }



    public static PrivateKey loadPrivateKeyFromPem(String pem) {
        if (pem != null && pem.contains("-----BEGIN RSA PRIVATE KEY-----")) {
            throw new IllegalArgumentException(
                    "PKCS#1 private key is not supported directly. Convert it to PKCS#8: -----BEGIN PRIVATE KEY-----");
        }

        validatePemBlock(pem, BEGIN_PRIVATE_KEY, END_PRIVATE_KEY, "RSA private key");
        byte[] der = decodePemBlock(pem, BEGIN_PRIVATE_KEY, END_PRIVATE_KEY);
        return loadPrivateKeyFromDer(der);
    }

    public static X509Certificate loadCertificateFromPem(String pem) {
        validatePemBlock(pem, BEGIN_CERTIFICATE, END_CERTIFICATE, "X.509 certificate");
        byte[] der = decodePemBlock(pem, BEGIN_CERTIFICATE, END_CERTIFICATE);
        return loadCertificate(der);
    }

    // =========================
    // Public API - load from DER bytes
    // =========================

    public static PublicKey loadPublicKeyFromDer(byte[] derBytes) {
        Objects.requireNonNull(derBytes, "derBytes must not be null");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
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

    public static PrivateKey loadPrivateKeyFromDer(byte[] derBytes) {
        Objects.requireNonNull(derBytes, "derBytes must not be null");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(derBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(spec);

            if (!(privateKey instanceof RSAPrivateKey)) {
                throw new IllegalArgumentException("Provided private key is not an RSA private key");
            }
            return privateKey;
        } catch (GeneralSecurityException exception) {
            throw new IllegalArgumentException("Invalid RSA private key content", exception);
        }
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

    public static KeyPair loadKeyPair(Path publicKeyPath, Path privateKeyPath) {
        PublicKey publicKey = loadPublicKey(publicKeyPath);
        PrivateKey privateKey = loadPrivateKey(privateKeyPath);
        return new KeyPair(publicKey, privateKey);
    }

    public static KeyPair loadKeyPair(String publicKeyPem, String privateKeyPem) {
        PublicKey publicKey = loadPublicKeyFromPem(publicKeyPem);
        PrivateKey privateKey = loadPrivateKeyFromPem(privateKeyPem);
        return new KeyPair(publicKey, privateKey);
    }

    public static KeyPair loadKeyPairFromCertificate(Path certificatePath, Path privateKeyPath) {
        PublicKey publicKey = loadCertificate(certificatePath).getPublicKey();
        PrivateKey privateKey = loadPrivateKey(privateKeyPath);

        if (!(publicKey instanceof RSAPublicKey)) {
            throw new IllegalArgumentException("Certificate does not contain an RSA public key");
        }
        return new KeyPair(publicKey, privateKey);
    }

    private static boolean isPem(byte[] content) {
        if (content == null || content.length == 0) {
            return false;
        }
        String text = new String(content, StandardCharsets.UTF_8);
        return text.contains("-----BEGIN ");
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

    private static String toPem(String beginMarker, String endMarker, byte[] encoded) {
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.UTF_8))
                .encodeToString(encoded);
        return beginMarker + "\n" + base64 + "\n" + endMarker;
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

    public static boolean verify(String data, String signatureBase64, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");

            signature.initVerify(publicKey);
            signature.update(data.getBytes());

            byte[] signBytes = Base64.getDecoder().decode(signatureBase64);

            return signature.verify(signBytes);
        } catch (Exception e) {
            throw new RuntimeException("Verify error", e);
        }
    }

    public static boolean verifyECDSA(String data, String signatureStr, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(signatureStr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(String data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signBytes);
        } catch (Exception e) {
            throw new RuntimeException("Sign error", e);
        }
    }

    public static String signECDSA(String data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
