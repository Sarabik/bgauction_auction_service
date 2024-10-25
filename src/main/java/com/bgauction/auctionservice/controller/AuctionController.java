package com.bgauction.auctionservice.controller;

import com.bgauction.auctionservice.model.dto.AuctionCreationDto;
import com.bgauction.auctionservice.model.dto.AuctionDto;
import com.bgauction.auctionservice.model.mapper.AuctionMapper;
import com.bgauction.auctionservice.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionMapper auctionMapper;
    private static final String AUCTION_ID_GREATER_THEN_0 = "Auction id must be greater then 0";
    private static final String GAME_ID_GREATER_THEN_0 = "Game id must be greater then 0";
    private static final String SELLER_ID_GREATER_THEN_0 = "Seller id must be greater then 0";
    private static final String CURRENT_PRICE_GREATER_THEN_0 = "Current price must be greater then 0";
    private static final String WINNER_ID_GREATER_THEN_0 = "Winner id must be greater then 0";

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuctionById(@PathVariable Long id) {
        if (id < 1) {
            return new ResponseEntity<>(AUCTION_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        AuctionDto auction = auctionMapper.auctionToAuctionDto(auctionService.findAuctionById(id));
        return new ResponseEntity<>(auction, HttpStatus.OK);
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> getAuctionByGameId(@PathVariable Long gameId) {
        if (gameId < 1) {
            return new ResponseEntity<>(GAME_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        AuctionDto auction = auctionMapper.auctionToAuctionDto(auctionService.findAuctionByGameId(gameId));
        return new ResponseEntity<>(auction, HttpStatus.OK);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getAuctionsBySellerId(@PathVariable Long sellerId) {
        if (sellerId < 1) {
            return new ResponseEntity<>(SELLER_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        List<AuctionDto> auctions = auctionService.findAllAuctionsBySellerId(sellerId).stream()
                .map(auctionMapper::auctionToAuctionDto).toList();
        return ResponseEntity.ok(auctions);
    }

    @GetMapping("/active")
    public ResponseEntity<List<AuctionDto>> getAllActiveAuctions() {
        List<AuctionDto> activeAuctions = auctionService.findAllActiveAuctions().stream()
                .map(auctionMapper::auctionToAuctionDto).toList();
        return ResponseEntity.ok(activeAuctions);
    }

    @PostMapping
    public ResponseEntity<?> createAuction(@Valid @RequestBody AuctionCreationDto auctionDto) {
        AuctionDto savedAuction = auctionMapper.auctionToAuctionDto
                (auctionService.saveNewAuction(auctionMapper.auctionCreationDtoToAuction(auctionDto)));
        return ResponseEntity.ok(savedAuction);
    }

    @PutMapping("/{id}/price/{price}/winner/{winnerId}")
    public ResponseEntity<?> updateCurrentPriceAndWinner(
            @PathVariable Long id, @PathVariable BigDecimal price, @PathVariable Long winnerId) {
        if (id < 1) {
            return new ResponseEntity<>(AUCTION_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        if (!(price.compareTo(BigDecimal.ZERO) > 0)) {
            return new ResponseEntity<>(CURRENT_PRICE_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        if (winnerId < 1) {
            return new ResponseEntity<>(WINNER_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        auctionService.updateCurrentPriceAndWinner(id, price, winnerId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<?> finishAuction(@PathVariable Long id) {
        if (id < 1) {
            return new ResponseEntity<>(AUCTION_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        auctionService.finishAuction(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuction(@PathVariable Long id) {
        if (id < 1) {
            return new ResponseEntity<>(AUCTION_ID_GREATER_THEN_0, HttpStatus.BAD_REQUEST);
        }
        auctionService.deleteAuctionById(id);
        return ResponseEntity.noContent().build();
    }

}
