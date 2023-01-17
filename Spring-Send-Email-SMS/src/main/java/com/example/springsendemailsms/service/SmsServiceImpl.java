package com.example.springsendemailsms.service;


import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import static com.twilio.example.ValidationExample.ACCOUNT_SID;
import static com.twilio.example.ValidationExample.AUTH_TOKEN;

@Service
public class SmsServiceImpl implements ISmsService {
    public static void senSMS(String bodyMessage) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        PhoneNumber formNumber = new PhoneNumber("+17778889999");
        PhoneNumber toNumber = new PhoneNumber("+17778889999");
        Message message = Message.creator(formNumber, toNumber, bodyMessage).create();
        System.out.println(message);
    }

    public static void listener() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        ResourceSet<Message> messages = Message.reader().read();
        for (Message message : messages) {
            System.out.println(message.getSid() + " : " + message.getStatus());
        }
    }
}
