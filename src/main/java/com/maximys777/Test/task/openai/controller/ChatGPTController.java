package com.maximys777.Test.task.openai.controller;

import com.maximys777.Test.task.openai.dto.request.PromptRequest;
import com.maximys777.Test.task.openai.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping
    public String chat(@RequestBody PromptRequest request) {
        return chatGPTService.getChatResponse(request);
    }
}
