package com.maximys777.Test.task.trello.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TrelloConfig {

    @Value("${trello.api.key}")
    private String apiKey;

    @Value("${trello.api.token}")
    private String token;

    @Bean
    public RestClient trelloRestClient(RestClient.Builder builder) {
        return builder.baseUrl("https://api.trello.com/1").build();
    }

    public String authParams() {
        return "key=" + apiKey + "&token=" + token;
    }
}
