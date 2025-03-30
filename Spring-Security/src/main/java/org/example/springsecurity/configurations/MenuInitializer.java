package org.example.springsecurity.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MenuInitializer implements ApplicationRunner {
    private final RequestMappingHandlerMapping mapping;

    @Override
    public void run(ApplicationArguments args) {
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo key = entry.getKey();
            HandlerMethod value = entry.getValue();
            System.out.println("RequestMappingInfo: " + key);
            System.out.println("HandlerMethod: " + value);
        }
    }
}
