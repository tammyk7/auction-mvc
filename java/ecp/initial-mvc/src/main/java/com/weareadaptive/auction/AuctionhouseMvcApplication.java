package com.weareadaptive.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// todo : remove exclusion to enable security
@SpringBootApplication()
public class AuctionhouseMvcApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(AuctionhouseMvcApplication.class, args);
    }
}
