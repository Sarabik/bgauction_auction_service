package com.bgauction.auctionservice.kafka.deserializer;

import com.bgauction.auctionservice.kafka.event.BidCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Log4j2
public class EventDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EventDeserializer() {}

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.info("Received null data for deserialization in topic: {}", topic);
                return null;
            }
            Class<?> targetClass = getTargetClass(topic);
            return (T) objectMapper.readValue(data, targetClass);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void close() {
    }

    private Class<?> getTargetClass(String topic) {
        Class<?> targetClass;
        if ("bid-created-event-topic".equals(topic)) {
            targetClass = BidCreatedEvent.class;
        } else {
            throw new IllegalArgumentException("Topic doesn't exist: " + topic);
        }
        return targetClass;
    }
}
