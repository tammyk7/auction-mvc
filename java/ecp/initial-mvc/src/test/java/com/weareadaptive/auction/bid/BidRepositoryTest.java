package com.weareadaptive.auction.bid;

import com.weareadaptive.auction.IntegrationTest;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.auction.Auction;
import com.weareadaptive.auction.auction.AuctionRepository;
import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.bid.BidRepository;
import com.weareadaptive.auction.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.junit.jupiter.Container;
import com.weareadaptive.auction.user.User;


import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BidRepositoryTest extends IntegrationTest
{
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    private User user1;

    // Initialise the PostgreSQL container
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            "postgres:latest").withDatabaseName("integration-tests-db").withUsername("testUsername").withPassword(
            "testPassword");

    // Dynamically replace the data source properties with those of the running container
    @DynamicPropertySource
    public static void postgresqlProperties(DynamicPropertyRegistry registry)
    {
        postgreSqlProperties(registry, postgreSQLContainer);
    }

    @BeforeEach
    public void setup()
    {
        this.user1 = userRepository.save(new User(
                "username1",
                "password",
                "First1",
                "Last1",
                "Org1")
        );
        final User user2 = userRepository.save(new User(
                "username2",
                "password",
                "First2",
                "Last2",
                "Org2")
        );

        final Auction auction = new Auction(
                user2,
                TestData.USDJPY,
                100,
                2.45
        );
        auctionRepository.save(auction);

        final Bid bid = new Bid(
                10,
                100.0,
                Instant.now(),
                user1,
                auction.getId()
        );
        bidRepository.save(bid);
    }

    @Test
    @DisplayName("should find user by user id")
    public void findByUserId()
    {
        final List<Bid> bids = bidRepository.findByUserId(user1.getId());

        assertThat(bids).isNotNull();
        assertThat(bids).hasSize(1);
        assertThat(bids.get(0).getUser().getId()).isEqualTo(user1.getId());
    }

}
