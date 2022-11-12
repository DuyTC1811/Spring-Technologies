package com.example.kafkaproducer.product;

import com.example.kafkaproducer.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.example.kafkaproducer.util.ConverterStringUntil.converterToString;

@Service
@Component
public class KafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${spring.topic-kafka.topic-example}")
    private String topic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void push(Customer customer) {
        String data = converterToString(customer);
        kafkaTemplate.send(topic, data);
        LOGGER.debug("Info {}", customer);
    }
}
