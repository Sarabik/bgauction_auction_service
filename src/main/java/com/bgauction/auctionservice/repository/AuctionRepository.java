package com.bgauction.auctionservice.repository;

import com.bgauction.auctionservice.model.entity.Auction;
import com.bgauction.auctionservice.model.entity.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findByGameId(Long gameId);
    List<Auction> findByStatus(AuctionStatus auctionStatus);
    List<Auction> findBySellerId(Long sellerId);

    @Query("SELECT a FROM Auction a WHERE a.endTime < :now AND a.status = 'ACTIVE'")
    List<Auction> findExpiredAuctions(@Param("now") LocalDateTime now);
}
