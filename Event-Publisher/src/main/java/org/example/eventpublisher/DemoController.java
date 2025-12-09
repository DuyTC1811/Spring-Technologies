package org.example.eventpublisher;

import lombok.RequiredArgsConstructor;
import org.example.eventpublisher.listener.DemoEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    private final ApplicationEventPublisher publisher;

    @RequestMapping("/publish-event")
    public String publishEvent() {
        publisher.publishEvent(new DemoEvent("Nguyen Van A"));
        return "Event published!";
    }
}
