package org.example.springsecurity.enums;

import lombok.Getter;

@Getter
public enum EMailTemplate {
    TEMPLATE_EMAIL_EXAMPLE("email-template.ftlh", "SUBJECT EXAMPLE"),
    ;

    private final String templateName;
    private final String subject;

    EMailTemplate(String templateName, String subject) {
        this.templateName = templateName;
        this.subject = subject;
    }

}
