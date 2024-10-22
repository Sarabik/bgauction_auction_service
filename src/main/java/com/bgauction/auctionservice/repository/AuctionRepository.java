package com.bgauction.auctionservice.repository;

import com.bgauction.auctionservice.model.entity.Auction;
import com.bgauction.auctionservice.model.entity.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findByGameId(Long gameId);
    List<Auction> findByStatus(AuctionStatus auctionStatus);
    List<Auction> findBySellerId(Long sellerId);
}
