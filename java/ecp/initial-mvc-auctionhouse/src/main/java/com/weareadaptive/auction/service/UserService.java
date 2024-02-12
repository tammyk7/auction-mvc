package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private final UserCollection userCollection;
    private final AuctionCollection auctionCollection;
    private final BidCollection bidCollection;

    public UserService(final UserCollection userCollection, final AuctionCollection auctionCollection, final BidCollection bidCollection)
    {
        this.userCollection = userCollection;
        this.auctionCollection = auctionCollection;
        this.bidCollection = bidCollection;
    }

    public User create(final String username, final String password, final String firstName, final String lastName, final String organisation)
    {
        final User user = new User(userCollection.nextId(), username, password, firstName, lastName, organisation);
        userCollection.add(user);
        return user;
    }

    public List<User> getAll()
    {
        return userCollection.stream().toList();
    }

    public User getUserById(final int id)
    {
        final User user = userCollection.getUser(id);

        if (user == null)
        {
            throw new BusinessException("user does not exist");
        }
        return user;
    }

    public void updateUserStatus(final int userId, final boolean isBlocked)
    {
        final User user = userCollection.getUser(userId);

        if (user == null)
        {
            throw new BusinessException("user does not exist");
        }
        user.setBlocked(isBlocked);
    }

    //updateUser

    public Optional<User> findByUsername(final String username)
    {
        if (!userCollection.containsUsername(username))
        {
            throw new BusinessException("user does not exist");
        }
        return userCollection.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    public Optional<User> validateUsernamePassword(final String username, final String password)
    {
        if (!userCollection.containsUsername(username))
        {
            throw new BusinessException("user does not exist");
        }
        return userCollection.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).filter(
                user -> user.validatePassword(password)).findFirst();
    }

    public List<Bid> getUserBids(final int userId)
    {
        if (userCollection.getUser(userId) == null)
        {
            throw new BusinessException("user does not exist");
        }
        final User user = userCollection.getUser(userId);
        return bidCollection.getUserBids(user.getId());
    }
}
