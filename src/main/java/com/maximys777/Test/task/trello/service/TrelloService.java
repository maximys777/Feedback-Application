package com.maximys777.Test.task.trello.service;

import com.maximys777.Test.task.feedback.entity.FeedbackEntity;
import com.maximys777.Test.task.trello.config.TrelloConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrelloService {

    private final RestClient trelloRestClient;
    private final TrelloConfig trelloConfig;

    @Value("${trello.list.id}")
    private String listId;

    public void createCardForFeedback(FeedbackEntity entity) {
        try {
            String[] params = trelloConfig.authParams().split("&");
            String key = params[0].split("=")[1];
            String token = params[1].split("=")[1];

            Map<String, Object> body = new HashMap<>();
            body.put("name", "Critical feedback " + entity.getDepartment());
            body.put("desc", """
                    Посада: %s
                    Філія: %s
                    Вігук: %s
                    Тип відгуку: %s
                    Критичність: %d
                    Рекомендація від AI: %s
                    """.formatted(
                    entity.getRole(),
                    entity.getDepartment(),
                    entity.getMessage(),
                    entity.getFeedbackType(),
                    entity.getCriticality(),
                    entity.getSuggestion()
            ));
            body.put("idList", listId);
            body.put("key", key);
            body.put("token", token);

            trelloRestClient.post()
                    .uri("/cards")
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
