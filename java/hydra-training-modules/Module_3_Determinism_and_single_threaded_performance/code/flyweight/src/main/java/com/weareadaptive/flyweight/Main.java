package com.weareadaptive.flyweight;

import java.util.Random;

// Client
public class Main
{
    private static final String[] COLORS = {"Red", "Green", "Blue", "Yellow", "Black"};
    private static final Random RANDOM = new Random();

    public static void main(final String[] args)
    {
        for (int i = 0; i < 10; i++)
        {
            try (final Circle circle = (Circle)ShapeFactory.getCircle(getRandomColor()))
            {
                circle.radius(RANDOM.nextInt(100));
                circle.draw();
            }
        }
    }

    private static String getRandomColor()
    {
        return COLORS[RANDOM.nextInt(COLORS.length)];
    }
}
