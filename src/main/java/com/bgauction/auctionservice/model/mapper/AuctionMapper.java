package com.bgauction.auctionservice.model.mapper;

import com.bgauction.auctionservice.kafka.event.AuctionUpdateEvent;
import com.bgauction.auctionservice.model.dto.AuctionCreationDto;
import com.bgauction.auctionservice.model.dto.AuctionDto;
import com.bgauction.auctionservice.model.entity.Auction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuctionMapper {
    Auction auctionCreationDtoToAuction(AuctionCreationDto dto);
    Auction auctionDtoToAuction(AuctionDto dto);
    AuctionDto auctionToAuctionDto(Auction entity);

    @Mapping(target = "createdAt", expression = "java(getCurrentTime())")
    @Mapping(source = "id", target = "auctionId")
    AuctionUpdateEvent auctionToAuctionUpdateEvent(Auction entity);

    default LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
