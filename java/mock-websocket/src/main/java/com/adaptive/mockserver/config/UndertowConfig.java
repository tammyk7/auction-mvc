package com.adaptive.mockserver.config;

import io.undertow.Undertow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.UndertowHttpHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

@Configuration
public class UndertowConfig {

    @Value("${server.port}")
    private int port;

    @Bean
    public Undertow undertowServer(ApplicationContext context) {

        HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context).build();

        UndertowHttpHandlerAdapter undertowAdapter = new UndertowHttpHandlerAdapter(handler);
        Undertow server = Undertow.builder()
                                  .addHttpListener(port, "localhost")
                                  .setHandler(undertowAdapter)
                                  .build();
        return server;
    }
}
