package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit
{
    private final UserRepository userRepository;

    public ApplicationInit(final UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitData()
    {
        System.out.println("hello tammy");
    }
}
