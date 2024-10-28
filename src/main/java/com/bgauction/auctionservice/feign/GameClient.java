package com.bgauction.auctionservice.feign;

import com.bgauction.auctionservice.model.dto.GameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "GAMESERVICE")
public interface GameClient {

    String URL_BASE = "/game/{id}";

    @GetMapping(URL_BASE)
    ResponseEntity<GameDto> getGameById(@PathVariable Long id);

    @PutMapping(URL_BASE + "/in_auction")
    ResponseEntity<Void> setStatusToInAuctionForGameWithId(@PathVariable Long id);

    @PutMapping(URL_BASE + "/sold")
    ResponseEntity<Void> setStatusToSoldForGameWithId(@PathVariable Long id);

    @PutMapping(URL_BASE + "/published")
    ResponseEntity<Void> setStatusToPublishedForGameWithId(@PathVariable Long id);
}
