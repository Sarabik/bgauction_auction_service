package com.bgauction.auctionservice.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;

@Data
@Builder
public class AuctionCreationDto {

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
    @Min(value = 1)
    @Max(value = 10080)
    private Long durationInMinutes;

}
