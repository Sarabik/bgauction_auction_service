package com.bgauction.auctionservice.service.impl;

import com.bgauction.auctionservice.exception.BadRequestException;
import com.bgauction.auctionservice.exception.NotFoundException;
import com.bgauction.auctionservice.feign.BidClient;
import com.bgauction.auctionservice.feign.GameClient;
import com.bgauction.auctionservice.model.dto.GameDto;
import com.bgauction.auctionservice.model.entity.Auction;
import com.bgauction.auctionservice.model.entity.AuctionStatus;
import com.bgauction.auctionservice.repository.AuctionRepository;
import com.bgauction.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final GameClient gameClient;
    private final BidClient bidClient;
    private static final String AUCTION_NOT_FOUND = "Auction with id: %d is not found";
    private static final String AUCTION_NOT_FOUND_BY_GAME_ID = "Auction for Game with id: %d is not found";
    private static final String GAME_NOT_FOUND = "Game with id: %d is not found";
    private static final String GAME_MUST_BE_PUBLISHED = "Status for Game with id: %d must be PUBLISHED";
    private static final String SELLER_IS_NOT_OWNER = "Seller id: %d is not equal to Game owner id: %d";
    private static final String CANT_DELETE_BIDS = "Bids for Auction with id: %d can't be deleted";
    private static final String CANT_CANCEL_NOT_ACTIVE_AUCTION = "Auction with id: %d can't be canceled because it is not ACTIVE";

    @Override
    public Auction findAuctionById(Long id) {
        return checkIfExistsAndReturnAuction(id);
    }

    @Override
    public Auction findAuctionByGameId(Long gameId) {
        Optional<Auction> optional = auctionRepository.findByGameId(gameId);
        if (optional.isEmpty()) {
            throw new NotFoundException(String.format(AUCTION_NOT_FOUND_BY_GAME_ID, gameId));
        }
        return optional.get();
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
            throw new BadRequestException(String.format(SELLER_IS_NOT_OWNER, auction.getSellerId(), game.getUserId()));
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
        if (!response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            throw new NotFoundException(String.format(GAME_NOT_FOUND, id));
        }
    }

    private GameDto getGameIfItExistAndPublished(Long gameId) {
        ResponseEntity<GameDto> response = gameClient.getGameById(gameId);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new NotFoundException(String.format(GAME_NOT_FOUND, gameId));
        } else {
            GameDto game = response.getBody();
            if (game == null) {
                throw new NotFoundException(String.format(GAME_NOT_FOUND, gameId));
            }
            if (!game.getStatus().equals("PUBLISHED")) {
                throw new BadRequestException(String.format(GAME_MUST_BE_PUBLISHED, gameId));
            }
            return game;
        }
    }

    @Override
    public void updateCurrentPriceAndWinner(Long id, BigDecimal price, Long winnerId) {
        //change exists check
        Auction auction = checkIfExistsAndReturnAuction(id);
        if (auction.getCurrentPrice().compareTo(price) < 0) {
            auction.setCurrentPrice(price);
            auction.setWinnerId(winnerId);
            auctionRepository.save(auction);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void closeExpiredAuctions() {
        List<Auction> expiredList = auctionRepository.findExpiredAuctions(LocalDateTime.now());
        if (!expiredList.isEmpty()) {
            for (Auction auction : expiredList) {
                finishAuction(auction);
            }
        }
    }

    public void finishAuction(Auction auction) {
        auction.setStatus(AuctionStatus.COMPLETED);
        auctionRepository.save(auction);
        changeGameStatusAfterAuctionFinished(auction);
        deleteAllBidsForAuction(auction.getId());
    }

    private void deleteAllBidsForAuction(Long id) {
        ResponseEntity<Void> response = bidClient.deleteBidsByAuctionId(id);
        if (!response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            throw new BadRequestException(String.format(CANT_DELETE_BIDS, id));
        }
    }

    private void changeGameStatusAfterAuctionFinished(Auction auction) {
        ResponseEntity<Void> response;
        if (auction.getWinnerId() == null) {
            response = gameClient.setStatusToPublishedForGameWithId(auction.getGameId());
        } else {
            response = gameClient.setStatusToSoldForGameWithId(auction.getGameId());
        }
        if (!response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            throw new NotFoundException(String.format(GAME_NOT_FOUND, auction.getGameId()));
        }
    }

    @Override
    public void cancelAuction(Long id) {
        Auction auction = checkIfExistsAndReturnAuction(id);
        if (!auction.getStatus().equals(AuctionStatus.ACTIVE)) {
            throw new BadRequestException(String.format(CANT_CANCEL_NOT_ACTIVE_AUCTION, id));
        }
        auction.setStatus(AuctionStatus.CANCELLED);
        ResponseEntity<Void> response = gameClient.setStatusToPublishedForGameWithId(auction.getGameId());
        if (!response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            throw new NotFoundException(String.format(GAME_NOT_FOUND, auction.getGameId()));
        }
    }

    private Auction checkIfExistsAndReturnAuction(Long id) {
        Optional<Auction> opt = auctionRepository.findById(id);
        if (opt.isEmpty()) {
            throw new NotFoundException(String.format(AUCTION_NOT_FOUND, id));
        }
        return opt.get();
    }
}
