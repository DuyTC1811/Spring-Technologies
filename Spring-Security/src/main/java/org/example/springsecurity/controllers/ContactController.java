package org.example.springsecurity.controllers;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.enums.EMailTemplate;
//import org.example.springsecurity.handlers.IMailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class ContactController {
//    private final IMailService mailService;

    @GetMapping("/contact")
    public String getContact() {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "DUYTC");
//        mailService.sendMail(EMailTemplate.TEMPLATE_EMAIL_EXAMPLE, "duytran.81811@gmail.com", model);
        return "This Contact Controller";
    }
}
