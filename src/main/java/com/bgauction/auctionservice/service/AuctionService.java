package com.bgauction.auctionservice.service;

import com.bgauction.auctionservice.model.entity.Auction;
import com.bgauction.auctionservice.model.entity.AuctionStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AuctionService {
    Optional<Auction> findAuctionById(Long id);
    Optional<Auction> findAuctionByGameId(Long game_id);
    List<Auction> findAllAuctionsBySellerId(Long seller_id);
    List<Auction> findAllActiveAuctions();
    Auction saveNewAuction(Auction auction);
    void updateCurrentPrice(Long id, BigDecimal price);
    void updateStatus(Long id, AuctionStatus status);
    void deleteAuctionById(Long id);
}
