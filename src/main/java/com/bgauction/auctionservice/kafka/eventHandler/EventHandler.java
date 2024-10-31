package com.bgauction.auctionservice.kafka.eventHandler;

import com.bgauction.auctionservice.kafka.event.BidCreatedEvent;
import com.bgauction.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class EventHandler {

    private final AuctionService auctionService;

    @KafkaListener(topics = "bid-created-event-topic")
    public void handleBidCreatedEvent(BidCreatedEvent event) {
        log.info("Received message with event: {}", event.toString());
        auctionService.updateCurrentPriceAndWinner(
                event.getAuctionId(), event.getBidAmount(), event.getBidderId());
    }
}
