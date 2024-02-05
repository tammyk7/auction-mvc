package com.weareadaptive.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// todo : remove exclusion to enable security
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AuctionHouseWebApplication
{

    public static void main(final String[] args)
    {
        SpringApplication.run(AuctionHouseWebApplication.class, args);
    }

}
