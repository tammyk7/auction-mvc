package com.weareadaptive.auction.user;

import com.weareadaptive.auction.RequestsResponses.UpdateUserRequest;
import com.weareadaptive.auction.auction.AuctionRepository;
import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.bid.BidRepository;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    public UserService(final UserRepository userRepository, final AuctionRepository auctionRepository, final BidRepository bidRepository)
    {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    public User create(String username, String password, String firstName, String lastName, String organisation)
    {
        User user = new User(username, password, firstName, lastName, organisation);
        return userRepository.save(user);
    }


    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public User getUserById(final int id)
    {
        final Optional<User> user = userRepository.findById(id);

        if (user.isEmpty())
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }
        return user.get();
    }

    public void updateUserStatus(final int userId, final boolean isBlocked)
    {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationExceptionHandling.BusinessException("user does not exist"));
        user.setBlocked(isBlocked);
        userRepository.save(user);
    }

    public void updateUser(final int userId, final UpdateUserRequest updateUserRequest)
    {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationExceptionHandling.BusinessException("user does not exist"));
        user.setFirstName(updateUserRequest.firstName());
        user.setLastName(updateUserRequest.lastName());
        user.setPassword(updateUserRequest.password());
        user.setOrganisation(updateUserRequest.organisation());
        userRepository.save(user);
    }

    public Optional<User> findByUsername(final String username)
    {
        return userRepository.findByUsername(username);
    }

    public Optional<User> validateUsernamePassword(final String username, final String password)
    {
        return userRepository.findByUsername(username)
                .filter(user -> user.validatePassword(password));
    }

    public List<Bid> getUserBids(final int userId)
    {
        if (!userRepository.existsById(userId))
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }
        return bidRepository.findByUserId(userId);
    }
}
