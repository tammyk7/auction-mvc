package com.weareadaptive.auctionhouse.console;

public class LoginMenu extends ConsoleMenu {
  private final AuctionMenu auctionMenu;
  private final UserMenu userMenu;

  public LoginMenu() {
    auctionMenu = new AuctionMenu();
    userMenu = new UserMenu();
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
                  option("User management", userMenu::display, c -> c.getCurrentUser().isAdmin()),
                  option("Auction management", auctionMenu::display),
                  leave("Log out")
          );
        }, () -> out.println("Invalid username/password combination"));
  }
}
