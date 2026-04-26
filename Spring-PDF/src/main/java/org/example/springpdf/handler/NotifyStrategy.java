package org.example.springpdf.handler;

import org.example.springpdf.model.NotifyRequest;
import org.example.springpdf.model.NotifyResult;
import org.example.springpdf.service.Strategy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class NotifyStrategy implements Strategy<NotifyRequest, NotifyResult> {
    @Override
    public String type() {
        return "EMAIL";
    }

    @Override
    public NotifyResult execute(NotifyRequest input) {
        return new NotifyResult();
    }
}
