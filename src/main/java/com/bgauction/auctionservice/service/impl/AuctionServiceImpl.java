package com.bgauction.auctionservice.service.impl;

import com.bgauction.auctionservice.model.entity.Auction;
import com.bgauction.auctionservice.model.entity.AuctionStatus;
import com.bgauction.auctionservice.repository.AuctionRepository;
import com.bgauction.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    @Override
    public Optional<Auction> findAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    @Override
    public Optional<Auction> findAuctionByGameId(Long gameId) {
        return auctionRepository.findByGameId(gameId);
    }

    @Override
    public List<Auction> findAllAuctionsBySellerId(Long sellerId) {
        return auctionRepository.findBySellerId(sellerId);
    }

    @Override
    public List<Auction> findAllActiveAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.ACTIVE);
    }

    @Override
    public Auction saveNewAuction(Auction auction) {
        auction.setCurrentPrice(auction.getStartPrice());
        auction.setEndTime(auction.getStartTime().plusMinutes(auction.getDurationInMinutes()));
        auction.setStatus(AuctionStatus.ACTIVE);
        return auctionRepository.save(auction);
    }

    @Override
    public void updateCurrentPrice(Long id, BigDecimal price) {
        auctionRepository.findById(id).ifPresent(auction -> {
            if (auction.getCurrentPrice().compareTo(price) < 0) {
                auction.setCurrentPrice(price);
                auctionRepository.save(auction);
            }
        });
    }

    @Override
    public void updateStatus(Long id, AuctionStatus status) {
        auctionRepository.findById(id).ifPresent(auction -> {
            auction.setStatus(status);
            auctionRepository.save(auction);
        });
    }

    @Override
    public void deleteAuctionById(Long id) {
        auctionRepository.deleteById(id);
    }
}
