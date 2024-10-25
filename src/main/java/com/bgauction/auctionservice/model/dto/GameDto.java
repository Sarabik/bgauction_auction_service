package com.bgauction.auctionservice.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GameDto {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    private String status;
}
