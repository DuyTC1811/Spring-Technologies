package org.example.springsecurity.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecurity.handlers.IDemoService;
import org.example.springsecurity.responses.CurrencyResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class DemoController {
    private final IDemoService demoService;
    @GetMapping("/exchange-rate")
    public CurrencyResponse getExchangeRateSpot() {
        return demoService.getExchangeRateSpot();
    }
}
