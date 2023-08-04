package com.weareadaptive.auction.repository;

import com.weareadaptive.auction.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("SELECT u FROM AuctionUser u WHERE u.username = ?1")
  Optional<User> findByUsername(String username);
}
