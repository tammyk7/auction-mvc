package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserCollection;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit
{
    private final UserCollection userCollection;

    public ApplicationInit(final UserCollection userCollection)
    {
        this.userCollection = userCollection;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitData()
    {
        var admin = new User(
                userCollection.nextId(),
                "ADMIN",
                "adminpassword",
                "admin",
                "admin",
                "Adaptive",
                true);

        userCollection.add(admin);

        System.out.println("hello tammy");
    }
}
