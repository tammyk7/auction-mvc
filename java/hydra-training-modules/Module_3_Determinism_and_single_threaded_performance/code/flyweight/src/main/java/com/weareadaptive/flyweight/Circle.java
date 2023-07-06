package com.weareadaptive.flyweight;

import java.io.IOException;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

// Concrete flyweight class
class Circle implements Shape
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(Circle.class);
    private final String color;
    private int radius;

    public Circle(final String color)
    {
        this.color = color;
    }

    public void radius(final int radius)
    {
        this.radius = radius;
    }

    @Override
    public void draw()
    {
        LOGGER.info("Drawing a circle with color ").append(color).append(", radius = ").append(radius).log();
    }

    @Override
    public void close()
    {
        radius = 0;
    }
}
