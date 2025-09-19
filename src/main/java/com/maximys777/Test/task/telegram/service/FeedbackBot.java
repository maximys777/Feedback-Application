package com.maximys777.Test.task.telegram.service;

import com.maximys777.Test.task.feedback.dto.request.FeedbackRequest;
import com.maximys777.Test.task.feedback.entity.common.FeedbackState;
import com.maximys777.Test.task.feedback.service.FeedbackService;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.HashMap;
import java.util.Map;

public class FeedbackBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final FeedbackService feedbackService;

    private final Map<Long, FeedbackState> chatState = new HashMap<>();
    private final Map<Long, FeedbackRequest> feedbackRequest = new HashMap<>();

    public FeedbackBot(String botToken, FeedbackService feedbackService) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.feedbackService = feedbackService;
    }


    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            FeedbackState currentState = chatState.getOrDefault(chatId, FeedbackState.START);

            SendMessage response = SendMessage.builder()
                    .chatId(chatId)
                    .text(message)
                    .build();

            FeedbackRequest request = feedbackRequest.getOrDefault(chatId, new FeedbackRequest());

            switch (currentState) {
                case START:
                    if (message.equals("/start")) {
                        response.setText("Please type your role.");
                        chatState.put(chatId, FeedbackState.AWAITING_ROLE);
                    } else {
                        response.setText("Please type /start to begin.");
                    }
                    break;
                case AWAITING_ROLE:
                    request.setRole(message);
                    feedbackRequest.put(chatId, request);
                    response.setText("Please type your department.");
                    chatState.put(chatId, FeedbackState.AWAITING_DEPARTMENT);
                    break;
                case AWAITING_DEPARTMENT:
                    request.setDepartment(message);
                    feedbackRequest.put(chatId, request);
                    response.setText("Please type your message.");
                    chatState.put(chatId, FeedbackState.AWAITING_MESSAGE);
                    break;
                case AWAITING_MESSAGE:
                    request.setMessage(message);
                    feedbackRequest.put(chatId, request);
                    response.setText("Thank you for your feedback.");
                    feedbackService.createNewFeedback(request);
                    chatState.remove(chatId);
                    feedbackRequest.remove(chatId);
                    break;
            }

            try {
                telegramClient.execute(response);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
