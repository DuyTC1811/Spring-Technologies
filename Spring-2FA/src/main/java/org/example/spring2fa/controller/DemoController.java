package org.example.spring2fa.controller;

import lombok.RequiredArgsConstructor;
import org.example.spring2fa.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final UserService userService;
    @GetMapping("/public/ping")
    public String ping() {
        return "OK (public)";
    }

    @GetMapping("/me")
    public String me() {
        return "OK (authenticated + MFA factors)";
    }

    @GetMapping("/admin/secret")
    public String admin() {
        return "OK (ADMIN + MFA factors)";
    }

    @GetMapping("/ott/sent")
    public String ottSent() {
        return "OTT has been generated. Check application console log for the magic link.";
    }

    @GetMapping(value = "/qrcode/get/{username}")
    public String generateQRCode(@PathVariable("username") String userName) throws Throwable {
        String otpProtocol = userService.generateOtpProtocol(userName);
        System.out.println(otpProtocol);
        return userService.generateQrCode(otpProtocol);
    }

    @PostMapping(value = "/qrcode/validate/{username}")
    public boolean validateTotp(@PathVariable("username") String userName,  @RequestBody String requestJson) {
        return userService.validateTotp(userName, Integer.parseInt(requestJson));
    }
}
