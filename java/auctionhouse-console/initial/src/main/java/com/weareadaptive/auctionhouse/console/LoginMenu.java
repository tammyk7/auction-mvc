package com.weareadaptive.auctionhouse.console;

public class LoginMenu extends ConsoleMenu {

  public LoginMenu() {
  }

  @Override
  public void display(MenuContext context) {
    createMenu(
        context,
        option("Login", this::login),
        leave("Quit")
    );
  }

  private void login(MenuContext context) {
    var out = context.getOut();

    out.println("Enter your username:");
    var username = context.getScanner().nextLine();

    out.println("Enter your password:");
    var password = readPassword(context.getScanner());

    context.getState()
        .userState()
        .findUserByUsername(username, password)
        .ifPresentOrElse(user -> {
          context.setCurrentUser(user);
          out.printf("Welcome %s %s %n", user.getFirstName(), user.getLastName());
          createMenu(
                  context,
                  leave("Log out")
          );
        }, () -> out.println("Invalid username/password combination"));
  }
}
