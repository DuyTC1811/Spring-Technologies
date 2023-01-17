package com.example.springsendemailsms.entity;

import lombok.Data;

@Data
public class SendInfo {
    private String from;
    private String to;
    private String subject;
    private String text;
}
