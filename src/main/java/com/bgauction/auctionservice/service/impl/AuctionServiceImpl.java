package com.bgauction.auctionservice.service.impl;

import com.bgauction.auctionservice.feign.BidClient;
import com.bgauction.auctionservice.feign.GameClient;
import com.bgauction.auctionservice.model.dto.GameDto;
import com.bgauction.auctionservice.model.entity.Auction;
import com.bgauction.auctionservice.model.entity.AuctionStatus;
import com.bgauction.auctionservice.repository.AuctionRepository;
import com.bgauction.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final GameClient gameClient;
    private final BidClient bidClient;

    @Override
    public Optional<Auction> findAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    @Override
    public Optional<Auction> findAuctionByGameId(Long gameId) {
        return auctionRepository.findByGameId(gameId);
    }

    @Override
    public List<Auction> findAllAuctionsBySellerId(Long sellerId) {
        return auctionRepository.findBySellerId(sellerId);
    }

    @Override
    public List<Auction> findAllActiveAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.ACTIVE);
    }

    @Override
    public Auction saveNewAuction(Auction auction) {
        GameDto game = getGameIfItExistAndPublished(auction.getGameId());
        if (!game.getUserId().equals(auction.getSellerId())) {
            throw new RuntimeException(
                    "Seller id " + auction.getSellerId() + " is not equal to game owner id " + game.getUserId());
        }
        auction.setCurrentPrice(auction.getStartPrice());
        auction.setStartTime(LocalDateTime.now());
        auction.setEndTime(auction.getStartTime().plusMinutes(auction.getDurationInMinutes()));
        auction.setStatus(AuctionStatus.ACTIVE);
        Auction savedAuction = auctionRepository.save(auction);

        changeGameStatusToInAuction(game.getId());
        return savedAuction;
    }

    private void changeGameStatusToInAuction(Long id) {
        ResponseEntity<Void> response = gameClient.setStatusToInAuctionForGameWithId(id);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new RuntimeException(
                    "Status for Game with id " + id + " is not changed to IN_AUCTION");
        }
    }

    private GameDto getGameIfItExistAndPublished(Long gameId) {
        ResponseEntity<GameDto> response = gameClient.getGameById(gameId);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new RuntimeException("Game with id " + gameId + " not found");
        }
        GameDto game = response.getBody();
        if (game == null) {
            throw new RuntimeException("Game with id " + gameId + " not found");
        }
        if (!game.getStatus().equals("PUBLISHED")) {
            throw new RuntimeException("Game status must be PUBLISHED");
        }
        return game;
    }

    @Override
    public void updateCurrentPriceAndWinner(Long id, BigDecimal price, Long winnerId) {
        auctionRepository.findById(id).ifPresent(auction -> {
            if (auction.getCurrentPrice().compareTo(price) < 0) {
                auction.setCurrentPrice(price);
                auction.setWinnerId(winnerId);
                auctionRepository.save(auction);
            } else {
                throw new RuntimeException("New price must be greater then current price");
            }
        });
    }

    @Override
    public void deleteAuctionById(Long id) {
        checkIfExistsById(id);
        auctionRepository.deleteById(id);
    }

    @Override
    public void finishAuction(Long id) {
        Optional<Auction> opt = findAuctionById(id);
        if (opt.isEmpty()) {
            throw new RuntimeException("Auction with ID " + id + " does not exist");
        }
        Auction auction = opt.get();
        auction.setStatus(AuctionStatus.COMPLETED);
        auctionRepository.save(auction);
        changeGameStatusAfterAuctionFinished(auction);
        deleteAllBidsForAuction(auction.getId());
    }

    private void deleteAllBidsForAuction(Long id) {
        ResponseEntity<Void> response = bidClient.deleteBidsByAuctionId(id);
        if (!response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            throw new RuntimeException("Bids for Auction with ID " + id + " are not deleted");
        }
    }

    private void changeGameStatusAfterAuctionFinished(Auction auction) {
        if (auction.getWinnerId() == null) {
            ResponseEntity<Void> response = gameClient.setStatusToPublishedForGameWithId(auction.getGameId());
            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                throw new RuntimeException("Status for Game with ID " + auction.getGameId() + " not updated");
            }
        } else {
            gameClient.setStatusToSoldForGameWithId(auction.getGameId());
        }
    }

    @Override
    public void cancelAuction(Long id) {
        // do I need this?
    }

    private void checkIfExistsById(Long id) {
        if (!auctionRepository.existsById(id)) {
            throw new RuntimeException("Auction with ID " + id + " does not exist");
        }
    }
}
