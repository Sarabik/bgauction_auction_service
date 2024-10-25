package com.bgauction.auctionservice.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "auctions", uniqueConstraints = {@UniqueConstraint(columnNames = {"game_id", "id"})})
@AllArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Positive
    @Column(name = "seller_id", updatable = false)
    private Long sellerId;

    @NotNull
    @Positive
    @Column(name = "game_id", updatable = false)
    private Long gameId;

    @NotNull
    @Positive
    @Column(name = "start_price", updatable = false)
    private BigDecimal startPrice;

    @NotNull
    @Positive
    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @NotNull
    @Column(name = "start_time", updatable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time", updatable = false)
    private LocalDateTime endTime;

    @NotNull
    @Min(value = 1)
    @Max(value = 10080)
    @Column(name = "duration_in_minutes", updatable = false)
    private Long durationInMinutes;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AuctionStatus status = AuctionStatus.ACTIVE;

    @Column(name = "winner_id")
    private Long winnerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Objects.equals(id, auction.id) && Objects.equals(sellerId, auction.sellerId)
                && Objects.equals(gameId, auction.gameId) && Objects.equals(startPrice, auction.startPrice)
                && Objects.equals(currentPrice, auction.currentPrice) && Objects.equals(startTime, auction.startTime)
                && Objects.equals(endTime, auction.endTime) && Objects.equals(durationInMinutes, auction.durationInMinutes)
                && status == auction.status && Objects.equals(winnerId, auction.winnerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sellerId, gameId, startPrice, currentPrice, startTime, endTime, durationInMinutes, status, winnerId);
    }
}
