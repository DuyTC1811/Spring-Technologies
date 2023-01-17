package com.example.springsendemailsms.service;

import com.example.springsendemailsms.entity.SendInfo;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class IEmailServiceImpl implements IEmailService {
    private static final String NOREPLY_ADDRESS = "ferssken@gmail.com";
    private static final String TO_ADDRESS = "ferssken99@gmail.com";
    private final JavaMailSender sender;

    public IEmailServiceImpl(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendEmail(SendInfo sendInfo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(TO_ADDRESS);
            message.setSubject("BABBB");
            message.setText("Duy TEst");
            sender.send(message);
        } catch (MailException e) {
            System.err.println(e.getLocalizedMessage());
        }

    }
}
