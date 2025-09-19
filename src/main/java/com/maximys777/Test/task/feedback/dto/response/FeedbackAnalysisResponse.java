package com.maximys777.Test.task.feedback.dto.response;

import com.maximys777.Test.task.feedback.entity.common.FeedbackType;

public record FeedbackAnalysisResponse(FeedbackType feedbackType, int criticality, String suggestion) {
}
