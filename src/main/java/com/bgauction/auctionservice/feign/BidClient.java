package com.bgauction.auctionservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bid-service", url = "localhost:8300/bid")
public interface BidClient {
    @DeleteMapping("/auction/{auctionId}")
    ResponseEntity<Void> deleteBidsByAuctionId(@PathVariable Long auctionId);
}
