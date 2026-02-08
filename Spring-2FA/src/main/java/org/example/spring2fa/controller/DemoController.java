package org.example.spring2fa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
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
}
