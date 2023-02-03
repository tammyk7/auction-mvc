package com.weareadaptive.auctionhouse.console;

import java.util.Scanner;
import java.util.stream.Stream;

import com.weareadaptive.auctionhouse.model.ModelState;
import com.weareadaptive.auctionhouse.model.User;
import com.weareadaptive.auctionhouse.model.UserState;


public class ConsoleAuction {
  private final MenuContext menuContext;

  public ConsoleAuction() {
    var state = new ModelState(new UserState());
    initData(state);
    var scanner = new Scanner(System.in);
    menuContext = new MenuContext(state, scanner, System.out);
  }

  private void initData(ModelState state) {
    Stream.of(
        new User(state.userState().nextId(), "admin", "admin", "admin", "admin", "admin", true),
        new User(state.userState().nextId(), "jf", "mypassword", "JF", "Legault", "Org 1"),
        new User(state.userState().nextId(), "thedude", "biglebowski", "Walter", "Sobchak", "Org 2")
    ).forEach(u -> state.userState().add(u));
  }

  public void start() {
    LoginMenu loginMenu = new LoginMenu();
    loginMenu.display(menuContext);
  }

}
