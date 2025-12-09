package org.example.eventpublisher.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoorBellEventDispatcher {
    private final DemoEventHandler demoEventHandler;

    @EventListener
    public void dispatch(DemoEvent event) {
        demoEventHandler.handleEvent(event);
    }
}
