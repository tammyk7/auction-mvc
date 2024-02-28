package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.weareadaptive.auction.StringUtil.isNullOrEmpty;

@Entity(name = "auction")
public class Auction
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @OneToMany()
    @JoinColumn(name = "auction_id")
    private List<Bid> bids;
    //TODO: query the auction table with the user table - break the object relationship - instead do string username or int user id -
    // value from column in auction table and if u need users make separate query - separate repo calls
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String symbol;
    private int quantity;
    @Column(name = "minimum_price", columnDefinition = "numeric")
    private double minPrice;
    @Enumerated(value = EnumType.STRING)
    private AuctionStatus status;
    @Column(columnDefinition = "varchar")
    private BigDecimal totalRevenue;
    private int totalSoldQuantity;
    @Transient
    private List<Bid> winningBids;
    @Transient
    private List<Bid> losingBids;


    public Auction(final User user, final String symbol, final int quantity, final double minPrice)
    {
        if (quantity < 0)
        {
            throw new AuthenticationExceptionHandling.BusinessException("quantity has to be greater than 0");
        }
        if (minPrice < 0)
        {
            throw new AuthenticationExceptionHandling.BusinessException("accepted price has to be greater than 0.00");
        }
        if (isNullOrEmpty(symbol))
        {
            throw new AuthenticationExceptionHandling.BusinessException("symbol can not be null or empty");
        }
        if (user == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("user can not be null");
        }

        this.user = user;
        this.symbol = symbol;
        this.minPrice = minPrice;
        this.quantity = quantity;
        this.status = AuctionStatus.OPEN;
        this.bids = new ArrayList<>();
        this.totalRevenue = new BigDecimal(0);
        this.totalSoldQuantity = 0;
        this.winningBids = new ArrayList<>();
        this.losingBids = new ArrayList<>();
    }

    public Auction()
    {

    }

    public void makeBid(final Bid bid)
    {
        if (status == AuctionStatus.CLOSED)
        {
            throw new AuthenticationExceptionHandling.BusinessException("auction is closed");
        }

        if (bid.getPrice() <= minPrice)
        {
            throw new AuthenticationExceptionHandling.BusinessException("offer price is too low");
        }
        if (bid.getUser() == user)
        {
            throw new AuthenticationExceptionHandling.BusinessException("user can not bid on its own auction");
        }
        this.bids.add(bid);
    }

    public List<Bid> getWinningBids()
    {
        if (status == AuctionStatus.OPEN)
        {
            throw new AuthenticationExceptionHandling.BusinessException(
                    "You can only obtain the winning bids on a closed auction");
        }

        return winningBids;
    }

    public List<Bid> getLostBids()
    {
        if (status == AuctionStatus.OPEN)
        {
            throw new AuthenticationExceptionHandling.BusinessException(
                    "You can only obtain the losing bids on a closed auction");
        }
        return losingBids;
    }

    public void close()
    {
        this.status = AuctionStatus.CLOSED;

        final var sortedBids =
                bids.stream().sorted(Comparator.comparingDouble(Bid::getPrice)
                                .reversed()
                                .thenComparing(Bid::getTimestamp))
                        .toList();

        int remainingQuantity = this.quantity;

        for (final Bid bid : sortedBids)
        {
            if (remainingQuantity <= 0)
            {
                losingBids.add(bid);
            } else
            {
                final int bidQuantity = Math.min(remainingQuantity, bid.getQuantity());
                remainingQuantity -= bidQuantity;
                totalRevenue = totalRevenue.add(
                        BigDecimal.valueOf(bid.getPrice())
                                .multiply(BigDecimal.valueOf(bidQuantity))
                );
                totalSoldQuantity += bidQuantity;
                winningBids.add(bid);
            }
        }
    }

    public int getId()
    {
        return id;
    }

    public User getUser()
    {
        return user;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public boolean isOpen()
    {
        return status == AuctionStatus.OPEN;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public List<Bid> getBids()
    {
        return bids;
    }

    public double getMinPrice()
    {
        return minPrice;
    }

    public AuctionStatus getStatus()
    {
        return status;
    }

    public BigDecimal getTotalRevenue()
    {
        return totalRevenue;
    }

    public int getTotalSoldQuantity()
    {
        return totalSoldQuantity;
    }


    public enum AuctionStatus
    {
        OPEN, CLOSED
    }

}
