package com.bgauction.auctionservice.feign;

import com.bgauction.auctionservice.model.dto.GameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "game-service", url = "localhost:8100/game")
public interface GameClient {

    @GetMapping("/{id}")
    ResponseEntity<GameDto> getGameById(@PathVariable Long id);

    @PutMapping("/{id}/in_auction")
    ResponseEntity<Void> setStatusToInAuctionForGameWithId(@PathVariable Long id);

    @PutMapping("/{id}/sold")
    ResponseEntity<Void> setStatusToSoldForGameWithId(@PathVariable Long id);

    @PutMapping("/{id}/published")
    ResponseEntity<Void> setStatusToPublishedForGameWithId(@PathVariable Long id);
}
