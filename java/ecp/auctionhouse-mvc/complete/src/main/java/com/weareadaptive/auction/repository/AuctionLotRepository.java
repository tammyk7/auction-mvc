package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.AuctionLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionLotRepository extends JpaRepository<AuctionLot, Integer> {
}
