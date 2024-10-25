package com.bgauction.auctionservice.service;

import com.bgauction.auctionservice.model.entity.Auction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AuctionService {
    Optional<Auction> findAuctionById(Long id);
    Optional<Auction> findAuctionByGameId(Long game_id);
    List<Auction> findAllAuctionsBySellerId(Long seller_id);
    List<Auction> findAllActiveAuctions();
    Auction saveNewAuction(Auction auction);
    void updateCurrentPriceAndWinner(Long id, BigDecimal price, Long winnerId);
    void deleteAuctionById(Long id);
    void finishAuction(Long id);
    void cancelAuction(Long id);
}
