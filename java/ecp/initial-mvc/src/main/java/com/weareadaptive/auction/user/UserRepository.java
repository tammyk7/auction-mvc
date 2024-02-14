package com.weareadaptive.auction.user;

import com.weareadaptive.auction.user.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("SELECT u FROM AuctionUser u WHERE u.username = :username")
  Optional<User> findByUsername(String username);

  //creation update delte and get

}
//TODO: convert collection to repository and then add custom query
//TODO: write the table