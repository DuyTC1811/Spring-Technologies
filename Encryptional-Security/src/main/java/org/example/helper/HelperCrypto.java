package org.example.helper;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class HelperCrypto {
    // Encrypt payload with AES-GCM, wrap an AES key by Bank's RSA public key, sign with Client's RSA private key
    public static Map<String, String> encryptAndSign(String jsonPayload, PublicKey bankRsaPublicKey, PrivateKey clientRsaPrivateKey) {
        try {
            // 1) Generate AES-256 session key
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256);
            SecretKey aesKey = kg.generateKey();

            // 2) Random 12-byte IV for GCM
            byte[] iv = new byte[12];
            SecureRandom sr = SecureRandom.getInstanceStrong();
            sr.nextBytes(iv);

            // 3) AES-GCM encrypt
            byte[] cipherText = aesGcmEncrypt(aesKey, iv, jsonPayload.getBytes(StandardCharsets.UTF_8), null);

            // 4) Wrap AES key using RSA-OAEP (SHA-256)
            byte[] encKey = rsaOaepEncrypt(aesKey.getEncoded(), bankRsaPublicKey);

            // 5) Create a signature over (iv || encKey || cipherText)
            byte[] toSign = concat(iv, encKey, cipherText);
            byte[] signature = rsaSignSha256(toSign, clientRsaPrivateKey);

            // 6) Build transfer object (Base64)
            Map<String, String> out = new LinkedHashMap<>();
            out.put("iv", b64(iv));
            out.put("encKey", b64(encKey));
            out.put("cipherText", b64(cipherText));
            out.put("signature", b64(signature));
            return out;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not supported", e);
        }

    }

    public static String verifyAndDecrypt(Map<String, String> req, PrivateKey bankRsaPrivateKey, PublicKey clientRsaPublicKey) {
        byte[] iv = b64d(req.get("iv"));
        byte[] encKey = b64d(req.get("encKey"));
        byte[] cipherText = b64d(req.get("cipherText"));
        byte[] signature = b64d(req.get("signature"));

        // 1) Verify signature
        byte[] signed = concat(iv, encKey, cipherText);
        boolean ok = rsaVerifySha256(signed, signature, clientRsaPublicKey);
        if (!ok) {
            throw new SecurityException("Invalid signature");
        }

        // 2) Unwrap an AES key
        byte[] aesRaw = rsaOaepDecrypt(encKey, bankRsaPrivateKey);
        SecretKey aesKey = new SecretKeySpec(aesRaw, "AES");

        // 3) AES-GCM decrypt
        byte[] plain = aesGcmDecrypt(aesKey, iv, cipherText, null);
        return new String(plain, StandardCharsets.UTF_8);
    }


    public static byte[] aesGcmEncrypt(SecretKey key, byte[] iv, byte[] plaintext, byte[] aad) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcm = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcm);
            if (aad != null) cipher.updateAAD(aad);
            return cipher.doFinal(plaintext); // ciphertext || tag(16B) at the end
            // If you need tag separately, split the last 16 bytes.
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Padding scheme not supported", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not supported", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Invalid algorithm parameters", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key", e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("Illegal block size", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("Bad padding", e);
        }
    }


    public static byte[] aesGcmDecrypt(SecretKey key, byte[] iv, byte[] ciphertext, byte[] aad) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcm = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcm);
            if (aad != null) cipher.updateAAD(aad);
            return cipher.doFinal(ciphertext);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Padding scheme not supported", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not supported", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Invalid algorithm parameters", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key", e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("Illegal block size", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("Bad padding", e);
        }
    }


    public static byte[] rsaOaepEncrypt(byte[] data, PublicKey pub) {
        try {
            Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            OAEPParameterSpec spec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
            c.init(Cipher.ENCRYPT_MODE, pub, spec);
            return c.doFinal(data);
        } catch (NoSuchPaddingException ex) {
            throw new RuntimeException("Padding scheme not supported", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Algorithm not supported", ex);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new RuntimeException("Invalid algorithm parameters", ex);
        } catch (InvalidKeyException ex) {
            throw new RuntimeException("Invalid public key", ex);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            throw new RuntimeException("Encryption failed", ex);
        }
    }

    public static byte[] rsaOaepDecrypt(byte[] enc, PrivateKey priv) {
        try {
            Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            OAEPParameterSpec spec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
            c.init(Cipher.DECRYPT_MODE, priv, spec);
            return c.doFinal(enc);
        } catch (NoSuchPaddingException ex) {
            throw new RuntimeException("Padding scheme not supported", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Algorithm not supported", ex);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new RuntimeException("Invalid algorithm parameters", ex);
        } catch (InvalidKeyException ex) {
            throw new RuntimeException("Invalid private key", ex);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            throw new RuntimeException("Decryption failed", ex);
        }
    }

    public static byte[] rsaSignSha256(byte[] data, PrivateKey priv) {
        try {
            Signature s = Signature.getInstance("SHA256withRSA");
            s.initSign(priv);
            s.update(data);
            return s.sign();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not supported", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid private key", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Signature creation failed", e);
        }
    }

    public static boolean rsaVerifySha256(byte[] data, byte[] sig, PublicKey pub) {
        try {
            Signature s = Signature.getInstance("SHA256withRSA");
            s.initVerify(pub);
            s.update(data);
            return s.verify(sig);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not supported", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid public key", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Signature verification failed", e);
        }
    }


    public static String b64(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    public static byte[] b64d(String s) {
        return Base64.getDecoder().decode(s);
    }


    public static byte[] concat(byte[]... ares) {
        int len = 0;
        for (byte[] a : ares) len += a.length;
        byte[] out = new byte[len];
        int pos = 0;
        for (byte[] a : ares) {
            System.arraycopy(a, 0, out, pos, a.length);
            pos += a.length;
        }
        return out;
    }
}
