package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.Bid;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BidReposity extends JpaRepository<Bid, Integer> {

  @Query(value = "SELECT b FROM Bid b WHERE b.auctionLotId = ?1")
  List<Bid> getBidsForAuction(int auctionLotId);

  @Query(value = "SELECT b FROM Bid b WHERE b.auctionLotId = ?1 AND b.state = 'WIN'")
  List<Bid> getWinBidsForAuction(int auctionLotId);

  @Query(value = "SELECT b FROM Bid b WHERE b.username = ?1")
  List<Bid> getBidsForUser(String username);
}
