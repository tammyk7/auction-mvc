package com.weareadaptive.flyweight;

import java.util.HashMap;
import java.util.Map;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

// Flyweight factory
class ShapeFactory
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(ShapeFactory.class);
    private static final Map<String, Shape> CIRCLE_MAP = new HashMap<>();

    public static Shape getCircle(final String color)
    {
        return CIRCLE_MAP.computeIfAbsent(color, k ->
        {
            LOGGER.info("Creating a new circle with color: ").append(color).log();
            return new Circle(color);
        });
    }
}
