package org.example.springsecurity.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@UtilityClass
public class TOTPUtil {
    public static String generateSecret() {
        byte[] buffer = new byte[20]; // 160 bit — RFC 4226 recommended
        new SecureRandom().nextBytes(buffer);
        return new Base32().encodeAsString(buffer);
    }

    public static long genCode(byte[] secret, long timeIndex) {
        try {
            // 1. HMAC-SHA1
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(secret, "HmacSHA1"));
            byte[] hash = mac.doFinal(ByteBuffer.allocate(8).putLong(timeIndex).array());

            // 2. Dynamic Truncation
            int offset = hash[19] & 0xf;
            long code = ((hash[offset] & 0x7f) << 24)
                    | ((hash[offset + 1] & 0xff) << 16)
                    | ((hash[offset + 2] & 0xff) << 8)
                    | (hash[offset + 3] & 0xff);

            // 3. 6-digit OTP
            return code % 1_000_000;
        } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
            throw new RuntimeException("Failed to generate TOTP code", exception);
        }
    }

    public static void main(String[] args) {
//        System.out.println(generateSecret());
        // E5GX3TGOPM35XZTYICII5BBR65N7HBYH
        // 835639
        System.out.println(genCode(new Base32().decode("E5GX3TGOPM35XZTYICII5BBR65N7HBYH"), 1668222400L));

    }
}
