package org.example.springpdf.handler;

import org.example.springpdf.service.Strategy;
import org.springframework.stereotype.Service;

@Service
public class DemoStrategy implements Strategy<Void, Void> {
    @Override
    public String type() {
        return "demo";
    }

    @Override
    public Void execute(Void input) {
        System.out.println("执行了DemoStrategy");
        return null;
    }
}
