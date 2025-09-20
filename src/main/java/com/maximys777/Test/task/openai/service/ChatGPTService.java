package com.maximys777.Test.task.openai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximys777.Test.task.feedback.dto.response.FeedbackAnalysisResponse;
import com.maximys777.Test.task.feedback.entity.common.FeedbackType;
import com.maximys777.Test.task.openai.dto.request.ChatGPTRequest;
import com.maximys777.Test.task.openai.dto.request.PromptRequest;
import com.maximys777.Test.task.openai.dto.response.ChatGPTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    public String getChatResponse(PromptRequest request) {
        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(
                model,
                List.of(new ChatGPTRequest.Message("user", request.prompt()))
        );

        ChatGPTResponse response = restClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(chatGPTRequest)
                .retrieve()
                .body(ChatGPTResponse.class);

        assert response != null;
        return response.choices().getFirst().message().content();
    }

    public FeedbackAnalysisResponse analyzeFeedback(String message) {
        String prompt = buildPromptForFeedbackAnalysis(message);
        String raw = getChatResponse(new PromptRequest(prompt));
        String json = extractJson(raw);

        try {
            JsonNode root = objectMapper.readTree(json);

            String feedbackTypeRaw = root.path("feedback_type").asText("");
            int criticalityRaw = root.path("criticality").asInt(-1);
            String suggestionRaw = root.path("suggestion").asText("");

            FeedbackType feedbackType = FeedbackType.NEUTRAL;
            if ("POSITIVE".equalsIgnoreCase(feedbackTypeRaw)) feedbackType = FeedbackType.POSITIVE;
            else if ("NEGATIVE".equalsIgnoreCase(feedbackTypeRaw)) feedbackType = FeedbackType.NEGATIVE;

            if (criticalityRaw < 1 || criticalityRaw > 5) {
                criticalityRaw = CriticalityFallback(feedbackType, message);
            }

            return new FeedbackAnalysisResponse(feedbackType, criticalityRaw, suggestionRaw);

        } catch (Exception e) {
            return new FeedbackAnalysisResponse(FeedbackType.NEUTRAL, 3, "Не вдалося згенерувати пропозицію (fallback). Повторіть пізніше.");
        }
    }

    private String buildPromptForFeedbackAnalysis(String message) {
        return """
                Аналізуй це повідомлення від користувача. ПОВЕРНИ У СТРОКОВОМУ JSON (ТІЛЬКИ JSON):
                {
                  "feedbackType": "<POSITIVE|NEGATIVE|NEUTRAL>",
                  "criticality": <integer 1..5>,
                  "suggestion": "<коротка дія або рекомендація>"
                }
                
                Правила:
                - feedbackType: Потрібно оприділити тип вігуку: написати POSITIVE якщо позитивний відгук, написати NEGATIVE якщо скарга/негатив, написати NEUTRAL якщо нейтральний/побажання.
                - criticality: 1 — мінімально важливо, 5 — критично. Оціни по впливу / терміновості вирішення.
                - suggestion: 1-2 речення з чіткою дією (що зробити аби вирішити дане питання).
                Тепер проаналізуй текст нижче (після '---'):
                ---
                %s
                """.formatted(message);
    }


    private String extractJson(String raw) {
        if (raw == null) return "{}";

        int start = raw.indexOf("{");
        int end = raw.indexOf("}");

        if (start >= 0 && end >= 0 && end >= start) {
            return raw.substring(start, end + 1);
        } else {
            return raw;
        }
    }

    private int CriticalityFallback(FeedbackType feedbackType, String message) {
        if (feedbackType == FeedbackType.NEGATIVE) return 4;
        if (feedbackType == FeedbackType.POSITIVE) return 1;
        return 3;
    }
}
