package com.adaptive.mockserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = WebMvcAutoConfiguration.class)
@EnableScheduling
public class App {
    private static final Log logger = LogFactory.getLog(App.class);

    public static void main(String[] args) throws InterruptedException {

        logger.info("Starting app...");
        ConfigurableApplicationContext app = SpringApplication.run(App.class, args);
    }
}
