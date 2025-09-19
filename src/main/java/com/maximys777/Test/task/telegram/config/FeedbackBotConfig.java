package com.maximys777.Test.task.telegram.config;

import com.maximys777.Test.task.feedback.service.FeedbackService;
import com.maximys777.Test.task.telegram.service.FeedbackBot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
@RequiredArgsConstructor
public class FeedbackBotConfig {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final FeedbackService feedbackService;

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication() throws TelegramApiException {
        TelegramBotsLongPollingApplication bot = new TelegramBotsLongPollingApplication();
        bot.registerBot(botToken, new FeedbackBot(botToken, feedbackService));
        return bot;
    }
}
