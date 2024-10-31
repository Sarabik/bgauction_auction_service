package com.bgauction.auctionservice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidCreatedEvent {
    private Long auctionId;
    private Long bidderId;
    private BigDecimal bidAmount;
}
