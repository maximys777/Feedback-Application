package com.maximys777.Test.task.feedback.dto.request;

import lombok.Data;

@Data
public class FeedbackRequest {
    private String role;
    private String department;
    private String message;
}
