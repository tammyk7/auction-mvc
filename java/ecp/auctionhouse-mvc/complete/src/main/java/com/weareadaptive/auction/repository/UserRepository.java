package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("SELECT u FROM AuctionUser u WHERE u.username = ?1")
  Optional<User> findByUsername(String username);

  @Modifying
  @Query("UPDATE AuctionUser u SET u.isBlocked=TRUE WHERE u.id = ?1")
  int block(int userId);

  @Modifying
  @Query("UPDATE AuctionUser u SET u.isBlocked=FALSE WHERE u.id = ?1")
  int unblock(int userId);
}
