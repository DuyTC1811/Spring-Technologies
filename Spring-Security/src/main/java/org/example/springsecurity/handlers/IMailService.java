package org.example.springsecurity.handlers;

import org.example.springsecurity.enums.EMailTemplate;

import java.util.Map;

public interface IMailService {
    void sendMail(EMailTemplate template, String mailSendTo, Map<String, Object> model);

    void sendMailForgotPassword(String token, String emailSendTo);
}
