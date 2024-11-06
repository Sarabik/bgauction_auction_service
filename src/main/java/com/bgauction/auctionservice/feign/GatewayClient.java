package com.bgauction.auctionservice.feign;

import com.bgauction.auctionservice.model.dto.GameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "GATEWAY", configuration = ClientFeignConfig.class)
public interface GatewayClient {

    String URL_BASE = "/internal/game/{id}";

    @GetMapping(URL_BASE)
    ResponseEntity<GameDto> getGameById(@PathVariable Long id);

    @PutMapping(URL_BASE + "/in_auction")
    ResponseEntity<Void> setStatusToInAuctionForGameWithId(@PathVariable Long id);

    @PutMapping(URL_BASE + "/sold")
    ResponseEntity<Void> setStatusToSoldForGameWithId(@PathVariable Long id);

    @PutMapping(URL_BASE + "/published")
    ResponseEntity<Void> setStatusToPublishedForGameWithId(@PathVariable Long id);

    @DeleteMapping("/internal/bid/auction/{auctionId}")
    ResponseEntity<Void> deleteBidsByAuctionId(@PathVariable Long auctionId);
}
