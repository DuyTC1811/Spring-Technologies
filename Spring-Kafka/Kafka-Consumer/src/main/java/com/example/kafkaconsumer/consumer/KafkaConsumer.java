package com.example.kafkaconsumer.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "test", groupId = "group-id")
    public void consumer(String data) {
        System.out.println("Response Data " + data);
    }
}
