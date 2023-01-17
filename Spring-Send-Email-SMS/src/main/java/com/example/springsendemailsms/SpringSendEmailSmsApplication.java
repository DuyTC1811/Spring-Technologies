package com.example.springsendemailsms;

import com.example.springsendemailsms.entity.SendInfo;
import com.example.springsendemailsms.service.IEmailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

@SpringBootApplication
public class SpringSendEmailSmsApplication {
    private final IEmailService emailService;

    public SpringSendEmailSmsApplication(IEmailService emailService) {
        this.emailService = emailService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSendEmailSmsApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void triggerMail() {
//        emailService.sendEmail(new SendInfo());
//
//    }
}
