package com.bgauction.auctionservice.service.impl;

import com.bgauction.auctionservice.model.entity.Auction;
import com.bgauction.auctionservice.model.entity.AuctionStatus;
import com.bgauction.auctionservice.repository.AuctionRepository;
import com.bgauction.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        auction.setStartTime(LocalDateTime.now());
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
            } else {
                throw new RuntimeException("New price must be greater then current price");
            }
        });
    }

    @Override
    public void deleteAuctionById(Long id) {
        checkIfExistsById(id);
        auctionRepository.deleteById(id);
    }

    @Override
    public void finishAuction(Long id) {
        Optional<Auction> opt = findAuctionById(id);
        if (opt.isPresent()) {
            Auction auction = opt.get();
            auction.setStatus(AuctionStatus.COMPLETED);
            // add winner
            auctionRepository.save(auction);
        } else {
            throw new RuntimeException("Auction with ID " + id + " does not exist");
        }
    }

    @Override
    public void cancelAuction(Long id) {
        // do I need this?
    }

    private void checkIfExistsById(Long id) {
        if (!auctionRepository.existsById(id)) {
            throw new RuntimeException("Auction with ID " + id + " does not exist");
        }
    }
}
