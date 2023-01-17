package com.example.springsendemailsms.service;

import com.example.springsendemailsms.entity.SendInfo;

public interface IEmailService {
    void sendEmail(SendInfo sendInfo);
}
