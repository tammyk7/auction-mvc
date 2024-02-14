package com.weareadaptive.auction.user;

import com.weareadaptive.auction.RequestsResponses.UpdateUserRequest;
import com.weareadaptive.auction.auction.AuctionCollection;
import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.bid.BidCollection;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.user.UserRepository;
import com.weareadaptive.auction.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private UserRepository userRepository;
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
        return userRepository.findAll();
    }

    public User getUserById(final int id)
    {
        final User user = userCollection.getUser(id);

        if (user == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }
        return user;
    }

    public void updateUserStatus(final int userId, final boolean isBlocked)
    {
        final User user = userCollection.getUser(userId);

        if (user == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }
        user.setBlocked(isBlocked);
    }

    //updateUser

    public void updateUser(final int userId, final UpdateUserRequest updateUserRequest)
    {
        // validate user
        // user - update everything except username and id
        // user.setpassword (field)
        //userRep.save
    }

    public Optional<User> findByUsername(final String username)
    {
        if (!userCollection.containsUsername(username))
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }
        return userCollection.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    public Optional<User> validateUsernamePassword(final String username, final String password)
    {
        if (!userCollection.containsUsername(username))
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }
        return userCollection.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).filter(
                user -> user.validatePassword(password)).findFirst();
    }

    public List<Bid> getUserBids(final int userId)
    {
        if (userCollection.getUser(userId) == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }
        final User user = userCollection.getUser(userId);
        return bidCollection.getUserBids(user.getId());
    }

    public Optional<User> validateUsernamePassword(final String username, final String password)
    {
        if (!usernameIndex.containsKey(username))
        {
            return Optional.empty();
        }
        var user = usernameIndex.get(username);
        if (!user.validatePassword(password))
        {
            return Optional.empty();
        }
        return Optional.of(user);
    }

//    @Transactional
//    public User create(String username, String password, String firstName, String lastName,
//                       String organisation)
//    {
//        User user = new User(
//                username,
//                password,
//                firstName,
//                lastName,
//                organisation,
//                false);
//        try
//        {
//            userRepository.save(user);
//        } catch (DataIntegrityViolationException exception)
//        {
//            // todo: handle exception to return a business exception when username already exists
//        }
//        return user;
//    }
}
