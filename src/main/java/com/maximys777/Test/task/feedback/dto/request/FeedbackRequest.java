package com.maximys777.Test.task.feedback.dto.request;

import com.maximys777.Test.task.feedback.entity.common.FeedbackType;
import lombok.Data;

@Data
public class FeedbackRequest {
    private String role;
    private String department;
    private String message;
    private FeedbackType feedbackType;
    private Integer criticality;
}
