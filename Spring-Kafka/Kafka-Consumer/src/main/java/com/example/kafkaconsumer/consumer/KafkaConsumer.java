package com.example.kafkaconsumer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "test", groupId = "0")
    public void consumer(ConsumerRecord<String, String> record) {
        System.out.printf("Received message: topic=%s, partition=%d, offset=%d, key=%s, value=%s",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
    }
}
