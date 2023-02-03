package com.weareadaptive.auctionhouse.console;

import com.weareadaptive.auctionhouse.model.ModelState;
import com.weareadaptive.auctionhouse.model.User;
import java.io.PrintStream;
import java.util.Scanner;

public class MenuContext {
  private final ModelState state;
  private final Scanner scanner;
  private final PrintStream out;
  private User currentUser;

  public MenuContext(ModelState state, Scanner scanner, PrintStream out) {
    this.state = state;
    this.scanner = scanner;
    this.out = out;
  }

  public ModelState getState() {
    return state;
  }

  public Scanner getScanner() {
    return scanner;
  }

  public PrintStream getOut() {
    return out;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }
}
