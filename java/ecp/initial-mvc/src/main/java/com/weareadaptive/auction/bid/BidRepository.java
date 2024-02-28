package com.weareadaptive.auction.bid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer>
{
    @Query("SELECT b FROM bid b WHERE b.user.id = :userId")
    List<Bid> findByUserId(int userId);
}
