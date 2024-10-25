package com.bgauction.auctionservice.model.dto;

import com.bgauction.auctionservice.model.entity.AuctionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AuctionDto {

    private Long id;

    @NotNull
    @Positive
    private Long sellerId;

    @NotNull
    @Positive
    private Long gameId;

    @NotNull
    @Positive
    private BigDecimal startPrice;

    @NotNull
    @Positive
    private BigDecimal currentPrice;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    @Min(value = 1)
    @Max(value = 10080)
    private Long durationInMinutes;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AuctionStatus status = AuctionStatus.ACTIVE;

    private Long winnerId;
}
