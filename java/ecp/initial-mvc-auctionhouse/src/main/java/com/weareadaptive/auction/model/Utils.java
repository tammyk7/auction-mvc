package com.weareadaptive.auctionhouse.model;

import com.weareadaptive.auctionhouse.console.MenuContext;

import java.util.function.Predicate;

public class Utils
{
    public static String collectStringResponse(final MenuContext context, final String command)
    {
        context.getOut().println(command);
        return context.getScanner().nextLine();
    }

    public static Integer collectIntResponse(final MenuContext context, final String command)
    {
        context.getOut().println(command);
        return Integer.parseInt(context.getScanner().nextLine());
    }

    public static String getUserInput(final MenuContext context, final String ErrorMessage,
                                      final Predicate<String> predicate)
    {
        String input = context.getScanner().nextLine();
        while (!predicate.test(input))
        {
            context.getOut().println(ErrorMessage);
            input = context.getScanner().nextLine();
        }
        return input;
    }
}
