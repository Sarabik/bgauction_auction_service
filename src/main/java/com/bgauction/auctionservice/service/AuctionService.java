package com.bgauction.auctionservice.service;

import com.bgauction.auctionservice.model.entity.Auction;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AuctionService {
    Auction findAuctionById(Long id);
    Auction findAuctionByGameId(Long game_id);
    List<Auction> findAllAuctionsBySellerId(Long seller_id);
    List<Auction> findAllActiveAuctions();
    Auction saveNewAuction(Auction auction);
    void updateCurrentPriceAndWinner(Long id, BigDecimal price, Long winnerId) throws ExecutionException, InterruptedException;
    void cancelAuction(Long id);
}
