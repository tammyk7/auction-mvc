package com.weareadaptive.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// todo : remove exclusion to enable security
@SpringBootApplication()
public class AuctionHouseWebApplication
{

    public static void main(final String[] args)
    {
        SpringApplication.run(AuctionHouseWebApplication.class, args);
    }

}
