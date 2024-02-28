package com.weareadaptive.auction.security;

import com.weareadaptive.auction.user.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider
{

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(final Class<?> authentication)
    {

        return super.supports(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(
            final UserDetails userDetails,
            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException
    {
    }

    @Override
    protected UserDetails retrieveUser(
            final String userName,
            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException
    {

        final Object token = usernamePasswordAuthenticationToken.getCredentials();
        return Optional
                .ofNullable(token)
                .map(s -> getUser(String.valueOf(s)))
                .orElseThrow();
    }

    private UserDetails getUser(@NotNull final String token)
    {
        var splitIndex = token.indexOf(":");
        if (splitIndex < 1)
        {
            throw new BadCredentialsException("Bad token");
        }
        final var username = token.substring(0, splitIndex);
        final var password = token.substring(splitIndex + 1);
        final var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Bad token"));
        if (!user.validatePassword(password))
        {
            throw new BadCredentialsException("Bad token");
        }
        return User.builder()
                .username(user.getUsername())
                .password(password)
                .roles(user.isAdmin() ? "ADMIN" : "USER")
//                 .disabled(user.get().isBlocked())
                .build();
    }

}
