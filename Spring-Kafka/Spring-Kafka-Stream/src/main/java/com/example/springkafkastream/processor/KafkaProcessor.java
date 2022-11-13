package com.example.springkafkastream.processor;

import com.example.springkafkastream.entity.Customer;
import com.example.springkafkastream.util.ConverterStringUntil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProcessor {
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static Serde<Integer> INTEGER_SERDE = Serdes.Integer();
    private static Serde<Long> LONG_SERDE = Serdes.Long();

    @Autowired
    public void processorCustomer(final StreamsBuilder streamsBuilder) {
        KStream<String, String> source = streamsBuilder.stream("input", Consumed.with(STRING_SERDE, STRING_SERDE))
                .peek(((key, value) -> log.info(" key = " + key + ", value = " + value)));

        KStream<String, String> works = source.mapValues(value -> {
            Customer customer = ConverterStringUntil.converterStringToObject(value);
            customer.setLastName("DUY-TC");
            return ConverterStringUntil.converterToString(customer);
        });

        works.to("test");
    }
}
