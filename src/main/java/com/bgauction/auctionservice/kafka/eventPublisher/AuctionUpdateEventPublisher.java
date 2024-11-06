package com.bgauction.auctionservice.kafka.eventPublisher;

import com.bgauction.auctionservice.kafka.event.AuctionUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuctionUpdateEventPublisher {

    private final KafkaTemplate<String, AuctionUpdateEvent> kafkaTemplate;

    public void publishOutbidEvent(AuctionUpdateEvent event) throws ExecutionException, InterruptedException {
        sendMessage("outbid-event-topic", event);
    }

    public void publishNewBidEvent(AuctionUpdateEvent event) throws ExecutionException, InterruptedException {
        sendMessage("new-bid-event-topic", event);
    }

    public void publishAuctionFinishedEvent(AuctionUpdateEvent event) throws ExecutionException, InterruptedException {
        sendMessage("auction-finished-event-topic", event);
    }

    private void sendMessage(String topic, AuctionUpdateEvent event) throws ExecutionException, InterruptedException {
        SendResult<String, AuctionUpdateEvent> send = kafkaTemplate.send(topic, event).get();
        log.info("Send message with topic: {} and event: {}", send.getRecordMetadata().topic(), event);
    };
}
