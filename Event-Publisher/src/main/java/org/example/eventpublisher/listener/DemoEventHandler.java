package org.example.eventpublisher.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoEventHandler extends EventHandler<DemoEvent> {
    @Override
    protected void process(DemoEvent event) {
        log.info("Chó ngủ dậy!!!");
        log.info("Go go!! Có người tên là {} gõ cửa!", event.getMessage());
    }
}
