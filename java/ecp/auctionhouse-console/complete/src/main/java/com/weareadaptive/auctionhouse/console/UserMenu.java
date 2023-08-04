package com.weareadaptive.auctionhouse.console;

import com.weareadaptive.auctionhouse.model.BusinessException;
import com.weareadaptive.auctionhouse.model.User;

public class UserMenu extends ConsoleMenu {

  @Override
  public void display(MenuContext context) {
    createMenu(context,
        option("Create user", this::createUser),
        option("Show users", this::showUsers),
        option("Show organisations", this::showOrganisations),
        option("Show organisations details", this::showOrganisationsDetail),
        leave("Go back"));
  }

  private void showOrganisationsDetail(MenuContext context) {
    context.getOut().println("== Organisation Detail");
    context.getState()
        .userState()
        .getOrganisationsDetails()
        .forEach(o -> {
          context.getOut().printf("Organisation %s%n", o.organisationName());
          o.users().forEach(u -> context.getOut().printf("\tUsername: %s%n", u.getUsername()));
        });
    pressEnter(context);
  }

  private void createUser(MenuContext context) {
    try {
      context.getOut().println("=> User Creation");
      context.getOut().println("Enter the username:");
      final var username = context.getScanner().nextLine();
      context.getOut().println("Enter the password:");
      var password = readPassword(context.getScanner());
      context.getOut().println("Enter the same password:");
      var password1 = readPassword(context.getScanner());

      if (!password.equals(password1)) {
        context.getOut().println("Password must be the same");
        return;
      }

      context.getOut().println("Enter the first name:");
      var firstName = context.getScanner().nextLine();
      context.getOut().println("Enter the last name:");
      var lastName = context.getScanner().nextLine();
      context.getOut().println("Enter the organisation:");
      var organisation = context.getScanner().nextLine();
      var user = new User(
          context.getState().userState().nextId(),
          username,
          password,
          firstName,
          lastName,
          organisation
      );
      context.getState().userState().add(user);
      context.getOut().printf("User %s has been created %n", user.getId());
    } catch (BusinessException exception) {
      context.getOut().printf("Cannot create the user because %s", exception.getMessage());
    }
  }

  private void showUsers(MenuContext context) {
    context.getOut().println("== All users");
    context.getState()
        .userState()
        .stream()
        .forEach(u -> context.getOut()
            .printf("Id: %s, Username: %s, First name: %s, Last name: %s, Organisation: %s%n",
                u.getId(),
                u.getUsername(),
                u.getFirstName(),
                u.getLastName(),
                u.getOrganisation()));
    pressEnter(context);
  }

  private void showOrganisations(MenuContext context) {
    context.getOut().println("== All organisations");
    context.getState()
        .userState()
        .findOrganisations()
        .forEach(o -> context.getOut().println(o));
    pressEnter(context);
  }
}
