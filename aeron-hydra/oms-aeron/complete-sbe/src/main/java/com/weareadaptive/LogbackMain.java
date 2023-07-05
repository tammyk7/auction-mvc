package com.weareadaptive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackMain
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackMain.class);
        /**
         * The main method.
         *
         * @param args command line args
         */
        public static void main(final String[] args)
        {
            LOGGER.info("Logback Test");
        }
}
