package com.weareadaptive.auction.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.weareadaptive.auction.StringUtil.isNullOrEmpty;

public class Auction implements Entity
{
    private final int id;
    private final List<Bid> bids;
    private final User user;
    private final String symbol;
    private final int quantity;
    private final double minPrice;
    private AuctionStatus status;
    private BigDecimal totalRevenue;
    private int totalSoldQuantity;
    private final List<Bid> winningBids;
    private final List<Bid> losingBids;


    public Auction(final int id, final User user, final String symbol, final int quantity, final double minPrice)
    {
        if (quantity < 0)
        {
            throw new BusinessException("quantity has to be greater than 0");
        }
        if (minPrice < 0)
        {
            throw new BusinessException("accepted price has to be greater than 0.00");
        }
        if (isNullOrEmpty(symbol))
        {
            throw new BusinessException("symbol can not be null or empty");
        }
        if (user == null)
        {
            throw new BusinessException("user can not be null");
        }

        this.id = id;
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

    public void makeBid(final Bid bid)
    {
        if (status == AuctionStatus.CLOSED)
        {
            throw new BusinessException("auction is closed");
        }

        if (bid.getPrice() <= minPrice)
        {
            throw new BusinessException("offer price is too low");
        }
        if (bid.getUser() == user)
        {
            throw new BusinessException("user can not bid on its own auction");
        }
        this.bids.add(bid);
    }

    public List<Bid> getWinningBids()
    {
        if (status == AuctionStatus.OPEN)
        {
            throw new BusinessException("You can only obtain the winning bids on a closed auction");
        }

        return winningBids;
    }

    public List<Bid> getLostBids()
    {
        if (status == AuctionStatus.OPEN)
        {
            throw new BusinessException("You can only obtain the losing bids on a closed auction");
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

    @Override
    public int getId()
    {
        return id;
    }
}
