package com.bgauction.auctionservice.model.mapper;

import com.bgauction.auctionservice.model.dto.AuctionCreationDto;
import com.bgauction.auctionservice.model.dto.AuctionDto;
import com.bgauction.auctionservice.model.entity.Auction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuctionMapper {
    Auction auctionCreationDtoToAuction(AuctionCreationDto dto);
    Auction auctionDtoToAuction(AuctionDto dto);
    AuctionDto auctionToAuctionDto(Auction entity);
}
