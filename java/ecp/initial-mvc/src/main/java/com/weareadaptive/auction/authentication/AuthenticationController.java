package com.weareadaptive.auction.authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
//@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class AuthenticationController
{
    @PostMapping("/login")
    public void login()
    {

    }
    @PostMapping("/logout")
    public void logout()
    {

    }
}
