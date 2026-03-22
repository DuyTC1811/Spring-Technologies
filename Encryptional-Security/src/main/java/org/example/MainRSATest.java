package org.example;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.example.helper.HelperKeyUtils.EC;
import static org.example.helper.HelperKeyUtils.RSA;
import static org.example.helper.HelperKeyUtils.loadCertificateFromPem;
import static org.example.helper.HelperKeyUtils.loadPrivateKeyFromPem;
import static org.example.helper.HelperKeyUtils.loadResourceAsString;
import static org.example.helper.HelperKeyUtils.signature;
import static org.example.helper.HelperKeyUtils.verify;


public class MainRSATest {
    public static void main(String[] args) {
        // openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out private.pem
        // openssl pkey -in private.pem -pubout -out public.pem
        // openssl req -new -x509 -key private.pem -out cert.pem -days 365 -subj "/CN=Test RSA/O=Example/C=VN"

        String privatePem = loadResourceAsString("keys/RSA/private.pem");
        String certPem = loadResourceAsString("keys/RSA/cert.pem");
        // hoặc dùng public.pem:
        // String publicPem = loadResourceAsString("keys/public.pem");

        PrivateKey privateKey = loadPrivateKeyFromPem(privatePem, RSA);
        PublicKey publicKey = loadCertificateFromPem(certPem).getPublicKey();

        String data = "amount=1000&account=123456";

        String signature = signature(data, privateKey);
        System.out.println("[ SIGNATURE ] ::: " + signature);

        boolean valid = verify(data, signature, publicKey);
        System.out.println("[ VERIFY ] ::: " + valid);
    }
}
