package org.example;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Map;

import static org.example.helper.HelperCrypto.encryptAndSign;
import static org.example.helper.HelperCrypto.verifyAndDecrypt;

public class Main {
    public static void main(String[] args) throws Exception {
        // Generate RSA keypairs (demo). Thực tế: dùng cert của ngân hàng & client; nên đặt khóa trong HSM.
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(3072); // 2048 tối thiểu; 3072 khuyến nghị
        KeyPair bank = kpg.generateKeyPair();   // Bank RSA keypair
        KeyPair client = kpg.generateKeyPair(); // Client RSA keypair

        String payload = """
                {
                  "txnId": "TXN-2025-09-30-0001",
                  "amount": 259000,
                  "currency": "VND",
                  "accountNo": "1234567890",
                  "desc": "Topup mobile"
                }""";

        // ---- Client encrypts + signs ----
        Map<String, String> req = encryptAndSign(
                payload,
                bank.getPublic(),         // Ngân hàng public key
                client.getPrivate()       // Client private key để ký
        );

        // In ra "gói" gửi đi
        System.out.println("Request JSON to send:");
        System.out.println("{");
        System.out.println("  \"iv\": \"" + req.get("iv") + "\",");
        System.out.println("  \"encKey\": \"" + req.get("encKey") + "\",");
        System.out.println("  \"cipherText\": \"" + req.get("cipherText") + "\",");
        System.out.println("  \"signature\": \"" + req.get("signature") + "\"");
        System.out.println("}");

        // ---- Server verifies + decrypts ----
        String decrypted = verifyAndDecrypt(
                req,
                bank.getPrivate(),       // Ngân hàng private key để mở encKey
                client.getPublic()       // Client public key để verify chữ ký
        );

        System.out.println("\nDecrypted payload:");
        System.out.println(decrypted);
    }
}