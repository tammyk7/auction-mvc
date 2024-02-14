package com.weareadaptive.auction;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.RequestsResponses.CreateAuctionRequest;
import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.auction.AuctionService;
import com.weareadaptive.auction.user.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TestData
{
    public static final String ORG_1 = "Org 1";
    public static final String ORG_2 = "Org 2";
    public static final String AAPL = "AAPL";
    public static final String EBAY = "EBAY";
    public static final String FB = "FB";
    public static final String USDJPY = "USDJPY";

    public static final User ADMIN = new User(0, "admin", "admin", "admin", "admin", "admin", true);
    public static final User USER1 = new User(1, "testuser1", "password", "john", "doe", ORG_1);
    public static final User USER2 = new User(2, "testuser2", "password", "john", "smith", ORG_1);
    public static final User USER3 = new User(3, "testuser3", "password", "jane", "doe", ORG_2);
    public static final User USER4 = new User(4, "testuser4", "password", "naomie", "legault", ORG_2);


    public static final String PASSWORD = "mypassword";
    public static final String ADMIN_AUTH_TOKEN = "Bearer ADMIN:adminpassword";

    private final UserService userService;
    private final AuctionService auctionService;

    private final Faker faker;
    private User user1;

    public TestData(UserService userService, final AuctionService auctionService)
    {
        this.userService = userService;
        this.auctionService = auctionService;
        faker = new Faker();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitData()
    {
        user1 = createRandomUser();

        userService.create(
                "USER",
                "userpassword",
                "user",
                "user",
                "Adaptive1");

        auctionService.create(new CreateAuctionRequest(
                1,
                "aapl",
                20,
                3.00));

        auctionService.create(new CreateAuctionRequest(
                2,
                "fb",
                20,
                3.00));
    }

    public String user1Token()
    {
        return getToken(user1);
    }


    public User createRandomUser()
    {
        var name = faker.name();
        return userService.create(
                name.username().replace(".", ""),
                PASSWORD,
                name.firstName(),
                name.lastName(),
                faker.company().name()
        );

    }

    public String getToken(User user)
    {
        return "Bearer " + user.getUsername() + ":" + PASSWORD;
    }

}
