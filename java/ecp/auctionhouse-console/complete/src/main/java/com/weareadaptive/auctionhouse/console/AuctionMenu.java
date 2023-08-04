package com.weareadaptive.auctionhouse.console;

import static com.weareadaptive.auctionhouse.console.Parser.parseDouble;
import static com.weareadaptive.auctionhouse.console.Parser.parseInt;

import com.weareadaptive.auctionhouse.model.AuctionLot;
import com.weareadaptive.auctionhouse.model.BusinessException;
import java.util.function.Predicate;

public class AuctionMenu extends ConsoleMenu {

  @Override
  public void display(MenuContext context) {
    createMenu(
        context,
        option("Create an auction", this::createAuction),
        option("See your auctions", this::listAuctions),
        option("Close an auction", this::closeAuction),
        option("Bid", this::bid),
        option("Won bids", this::winBid),
        option("Lost bids", this::lostBid),
        leave("Go back")
    );
  }

  private void winBid(MenuContext context) {
    context.getState()
        .auctionState()
        .findWonBids(context.getCurrentUser())
        .forEach(wb -> context.getOut()
            .printf("AuctionLot: %s Symbol: %s Quantity: %s Bid Quantity: %s Price: %s",
                wb.auctionLotId(),
                wb.symbol(),
                wb.wonQuantity(),
                wb.bidQuantity(),
                wb.price()));
  }

  private void lostBid(MenuContext context) {
    context.getState()
        .auctionState()
        .stream()
        .filter(a -> AuctionLot.Status.CLOSED.equals(a.getStatus()))
        .forEach(a -> a.getBids()
            .stream()
            .filter(b -> b.user() == context.getCurrentUser()
                &&
                a.getClosingSummary().winningBids().stream().noneMatch(wb -> wb.originalBid() != b))
            .forEach(
                b -> context.getOut().printf("AuctionLot: %s Symbol: %s Quantity: %s Price: %s",
                    a.getId(),
                    a.getSymbol(),
                    b.quantity(),
                    b.price())
            )
        );
  }

  private void createAuction(MenuContext context) {
    var scanner = context.getScanner();
    var state = context.getState();
    var out = context.getOut();
    try {
      out.println("=> Auction Creation");
      out.println("Enter your symbol:");
      var symbol = scanner.nextLine();
      out.println("Enter your quantity:");
      var quantity = parseInt(scanner.nextLine(), "quantity");
      out.println("Enter your minimum price:");
      var minPrice = parseDouble(scanner.nextLine(), "minPrice");

      AuctionLot auction = new AuctionLot(
          state.auctionState().nextId(),
          context.getCurrentUser(),
          symbol,
          quantity,
          minPrice);
      state.auctionState().add(auction);
      out.printf("Auction id %s created! %n", auction.getId());
    } catch (ParsingException | BusinessException exception) {
      out.printf("Fail to create the auction because %s.%n", exception.getMessage());
    }
  }

  private void listAuctions(MenuContext context) {
    var out = context.getOut();
    out.println("=> Your Auctions");
    context.getState()
        .auctionState()
        .stream()
        .filter(a -> a.getOwner() == context.getCurrentUser())
        .forEach(a -> {
          out.println("============================");
          out.printf("Id: %s %n", a.getId());
          out.printf("Symbol: %s %n", a.getSymbol());
          out.printf("Status: %s %n", a.getStatus());

          out.println("All bids:");
          a.getBids().forEach(b -> out.printf("%s %n", b.toString()));

          if (AuctionLot.Status.CLOSED.equals(a.getStatus())) {
            out.printf("Total Revenue: %s %n", a.getClosingSummary().totalRevenue());
            out.printf("Total sold quantity: %s %n", a.getClosingSummary().totalSoldQuantity());
            out.println("Winning bids:");
            a.getClosingSummary()
                .winningBids()
                .forEach(wb -> {
                  out.printf("\tUser: %s %n", wb.originalBid().user().getUsername());
                  out.printf("\tTotal sold quantity: %s %n", wb.quantity());
                  out.printf("\tPrice: %s %n", wb.originalBid().price());
                });
          }
          out.println("============================");
          out.println();
        });
    pressEnter(context);
  }

  private void bid(MenuContext context) {
    var out = context.getOut();
    showAuction(
        context,
        a -> a.getOwner() != context.getCurrentUser()
            && AuctionLot.Status.OPENED.equals(a.getStatus()));
    try {
      out.println("Enter the auction id");
      var lotId = parseInt(context.getScanner().nextLine(), "auctionLotId");
      var lot = context.getState().auctionState().get(lotId);
      if (lot == null) {
        out.printf("AuctionLot %s doesn't exist %n", lotId);
        return;
      }
      out.println("Enter the quantity");
      var quantity = parseInt(context.getScanner().nextLine(), "quantity");
      out.println("Enter the price");
      var price = parseDouble(context.getScanner().nextLine(), "price");
      lot.bid(context.getCurrentUser(), quantity, price);
      out.println("Bid created");
    } catch (ParsingException | BusinessException exception) {
      out.printf("Invalid bid %s %n", exception.getMessage());
    }
  }

  private void showAuction(MenuContext context, Predicate<AuctionLot> predicate) {
    context
        .getState()
        .auctionState()
        .stream()
        .filter(predicate)
        .forEach(a -> context.getOut()
            .printf("\tAuction Id: %s | Title: %s %n", a.getId(), a.getSymbol()));
  }

  private void closeAuction(MenuContext context) {
    var out = context.getOut();

    showAuction(
        context,
        a -> a.getOwner() == context.getCurrentUser()
            && AuctionLot.Status.OPENED.equals(a.getStatus()));

    out.println("Enter the auction id");
    try {
      var lotId = parseInt(context.getScanner().nextLine(), "auctionLotId");
      var lot = context.getState().auctionState().get(lotId);
      if (lot == null) {
        out.printf("AuctionLot %s doesn't exist %n", lotId);
        return;
      }
      lot.close();
      out.printf("AuctionLot %s has been closed %n", lotId);
    } catch (ParsingException exception) {
      out.println(exception.getMessage());
    }
  }
}
