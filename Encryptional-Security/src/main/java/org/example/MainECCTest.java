package org.example;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.example.helper.HelperKeyUtils.EC;
import static org.example.helper.HelperKeyUtils.loadCertificateFromPem;
import static org.example.helper.HelperKeyUtils.loadPrivateKeyFromPem;
import static org.example.helper.HelperKeyUtils.loadResourceAsString;
import static org.example.helper.HelperKeyUtils.signature;
import static org.example.helper.HelperKeyUtils.verify;


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
        PrivateKey privateKey = loadPrivateKeyFromPem(privatePem, EC);

        // Cách 1: lấy từ cert
        PublicKey publicKey = loadCertificateFromPem(certPem).getPublicKey();

        // DATA
        String data = "amount=1000&account=123456";

        // SIGN
        String signature = signature(data, privateKey);
        System.out.println("[ SIGNATURE ] ::: " + signature);

        // VERIFY
        boolean valid = verify(data, signature, publicKey);
        System.out.println("[ VERIFY ]: " + valid);
    }
}
