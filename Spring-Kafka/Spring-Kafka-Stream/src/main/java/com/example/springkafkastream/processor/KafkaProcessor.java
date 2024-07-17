package com.example.springkafkastream.processor;

import com.example.springkafkastream.entity.Customer;
import com.example.springkafkastream.util.ConverterStringUntil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProcessor {
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<Integer> INTEGER_SERDE = Serdes.Integer();
    private static final Serde<Long> LONG_SERDE = Serdes.Long();

    private static final String INPUT_TOPIC = "input";
    private static final String OUTPUT_TOPIC = "output";

    @Autowired
    public void processorCustomer(final StreamsBuilder streamsBuilder) {
        // Read from input topic
        KStream<String, String> messageStream = streamsBuilder
                .stream(INPUT_TOPIC, Consumed.with(STRING_SERDE, STRING_SERDE))
                .peek((key, value) -> log.info("Received message: [ KEY ] = {}, [ VALUE ] = {}", key, value));

        // Process message
        KStream<String, String> works = messageStream.mapValues(value -> {
            if (value == null) {
                log.error("Received null value from Kafka topic");
                return null;
            }

            Customer customer = ConverterStringUntil.converterStringToObject(value);
            if (customer != null) {
                customer.setLastName("DUY-TC");
                return ConverterStringUntil.converterToString(customer);
            } else {
                log.error("Failed to convert value to Customer: {}", value);
                return value;
            }
        });

        // Write to output topic
        works.to(OUTPUT_TOPIC, Produced.with(STRING_SERDE, STRING_SERDE));
        works.to("test", Produced.with(STRING_SERDE, STRING_SERDE));
    }
}
