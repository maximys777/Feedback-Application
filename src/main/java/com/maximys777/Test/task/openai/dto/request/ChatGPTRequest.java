package com.maximys777.Test.task.openai.dto.request;

import java.util.List;

public record ChatGPTRequest(String model, List<Message> messages) {

    public static record Message(String role, String content){}
}
