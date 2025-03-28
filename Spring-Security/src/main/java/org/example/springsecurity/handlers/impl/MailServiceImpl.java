//package org.example.springsecurity.handlers.impl;
//
//
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import org.example.springsecurity.enums.EMailTemplate;
//import org.example.springsecurity.exceptions.BaseException;
//import org.example.springsecurity.handlers.IMailService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class MailServiceImpl implements IMailService {
//    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
//    private final JavaMailSender mailSender;
//    private final Configuration freemarkerConfig;
//
//    @Value("${spring.mail.mail-from}")
//    private String mailFrom;
//
//
//
//    // TODO @Async
//    @Override
//    public void sendMail(EMailTemplate template, String mailSendTo, Map<String, Object> model) {
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//            Template freemarkerTemplate = freemarkerConfig.getTemplate(template.getTemplateName());
//            String html = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
//
//            helper.setSubject(template.getSubject());
//            helper.setFrom(mailFrom);
//            helper.setTo(mailSendTo);
//            helper.setText(html, true);
//
//            /*
//            helper.addInline("header", new ClassPathResource("template/images/header.png"));
//            helper.addInline("footer", new ClassPathResource("template/images/footer.png"));
//            */
//
//            mailSender.send(mimeMessage);
//        } catch (MessagingException | IOException | TemplateException exception) {
//            LOGGER.error("[ SEND-MAIL-ERROR ] ", exception);
//            throw new BaseException(400, exception.getMessage());
//        }
//    }
//
//    @Override
//    public void sendMailForgotPassword(String token, String emailSendTo) {
//        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//        Map<String, Object> model = new HashMap<>();
//        model.put("token", token);
//        model.put("baseUrl", baseUrl);
//        sendMail(EMailTemplate.TEMPLATE_FOR_GOT_PASSWORD, emailSendTo, model);
//    }
//}
