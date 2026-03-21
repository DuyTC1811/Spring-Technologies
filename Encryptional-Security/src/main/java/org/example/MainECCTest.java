package org.example;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.example.helper.ECCKeyUtils.loadCertificateFromPem;
import static org.example.helper.ECCKeyUtils.loadPrivateKeyECCFromPem;
import static org.example.helper.RSAKeyUtils.loadResourceAsString;
import static org.example.helper.RSAKeyUtils.signECDSA;
import static org.example.helper.RSAKeyUtils.verifyECDSA;


public class MainECCTest {
    public static void main(String[] args) {

        // openssl genpkey -algorithm EC -pkeyopt ec_paramgen_curve:prime256v1 -out private.pem
        // openssl pkey -in private.pem -pubout -out public.pem
        // openssl req -new -x509 -key private.pem -out cert.pem -days 365 -subj "/CN=Test ECC/O=Example/C=VN"

        // LOAD FILE
        String privatePem = loadResourceAsString("keys/EC/private.pem");
        String certPem = loadResourceAsString("keys/EC/cert.pem");
        // hoặc dùng public.pem:
        // String publicPem = loadResourceAsString("keys/EC/public.pem");

        // LOAD KEY
        PrivateKey privateKey = loadPrivateKeyECCFromPem(privatePem);

        // Cách 1: lấy từ cert
        PublicKey publicKey = loadCertificateFromPem(certPem).getPublicKey();

        // DATA
        String data = "amount=1000&account=123456";

        // SIGN
        String signature = signECDSA(data, privateKey);
        System.out.println("[ SIGNATURE ] ::: " + signature);

        // VERIFY
        boolean valid = verifyECDSA(data, signature, publicKey);
        System.out.println("[ VERIFY ]: " + valid);
    }
}
