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
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionMapper auctionMapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuctionById(@PathVariable Long id) {
        if (id < 1) {
            return new ResponseEntity<>("Invalid Auction id", HttpStatus.BAD_REQUEST);
        }
        Optional<AuctionDto> auction = auctionService.findAuctionById(id).map(auctionMapper::auctionToAuctionDto);
        return auction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> getAuctionByGameId(@PathVariable Long gameId) {
        if (gameId < 1) {
            return new ResponseEntity<>("Invalid Game id", HttpStatus.BAD_REQUEST);
        }
        Optional<AuctionDto> auction = auctionService.findAuctionByGameId(gameId).map(auctionMapper::auctionToAuctionDto);
        return auction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getAuctionsBySellerId(@PathVariable Long sellerId) {
        if (sellerId < 1) {
            return new ResponseEntity<>("Invalid seller id", HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<AuctionDto> createAuction(@Valid @RequestBody AuctionCreationDto auctionDto) {
        AuctionDto savedAuction = auctionMapper.auctionToAuctionDto
                (auctionService.saveNewAuction(auctionMapper.auctionCreationDtoToAuction(auctionDto)));
        return ResponseEntity.ok(savedAuction);
    }

    @PutMapping("/{id}/price/{price}/winner/{winnerId}")
    public ResponseEntity<?> updateCurrentPriceAndWinner(
            @PathVariable Long id, @PathVariable BigDecimal price, @PathVariable Long winnerId) {
        if (id < 1) {
            return new ResponseEntity<>("Auction id must be greater then 0", HttpStatus.BAD_REQUEST);
        }
        if (!(price.compareTo(BigDecimal.ZERO) > 0)) {
            return new ResponseEntity<>("Current price must be greater then 0", HttpStatus.BAD_REQUEST);
        }
        if (winnerId < 1) {
            return new ResponseEntity<>("Last bidder id must be greater then 0", HttpStatus.BAD_REQUEST);
        }
        try {
            auctionService.updateCurrentPriceAndWinner(id, price, winnerId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<?> finishAuction(@PathVariable Long id) {
        if (id < 1) {
            return new ResponseEntity<>("Invalid Auction id", HttpStatus.BAD_REQUEST);
        }
        try {
            auctionService.finishAuction(id);
            return ResponseEntity.ok("Auction with ID " + id + " has been completed");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuction(@PathVariable Long id) {
        if (id < 1) {
            return new ResponseEntity<>("Invalid Auction id", HttpStatus.BAD_REQUEST);
        }
        try {
            auctionService.deleteAuctionById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
