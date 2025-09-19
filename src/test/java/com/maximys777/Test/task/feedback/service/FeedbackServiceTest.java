package com.maximys777.Test.task.feedback.service;

import com.maximys777.Test.task.feedback.dto.request.FeedbackRequest;
import com.maximys777.Test.task.feedback.dto.response.FeedbackAnalysisResponse;
import com.maximys777.Test.task.feedback.entity.FeedbackEntity;
import com.maximys777.Test.task.feedback.entity.common.FeedbackType;
import com.maximys777.Test.task.feedback.repository.FeedbackRepository;
import com.maximys777.Test.task.openai.service.ChatGPTService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private ChatGPTService chatGPTService;

    @InjectMocks
    private FeedbackService feedbackService;

    @Test
    void createNewFeedback_ShouldCreateNewFeedback_WhenSuccess() {
        FeedbackRequest request = new FeedbackRequest();
        request.setRole("Test");
        request.setDepartment("Test");
        request.setMessage("Test");

        FeedbackAnalysisResponse response = new FeedbackAnalysisResponse(FeedbackType.NEUTRAL, 1, "Test");

        Mockito.when(chatGPTService.analyzeFeedback("Test"))
                .thenReturn(response);

        feedbackService.createNewFeedback(request);


        Mockito.verify(feedbackRepository, Mockito.times(1))
                .save(Mockito.any(FeedbackEntity.class));
    }

    @Test
    void getAllByFilter_ShouldReturnAllFeedback_WhenSuccess() {
        String role = "Manager";
        String department = "IT";
        FeedbackType feedbackType = FeedbackType.NEUTRAL;
        Integer criticality = 5;
        LocalDateTime from = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 12, 31, 23, 59);
        Pageable pageable = PageRequest.of(0, 10);

        Page<FeedbackEntity> page = new PageImpl<>(List.of());

        Mockito.when(feedbackRepository.findAll(Mockito.<Specification<FeedbackEntity>>any(), Mockito.eq(pageable)))
                .thenReturn(page);

        Page<FeedbackEntity> result = feedbackService.getAllByFilter(role, department, feedbackType,
                criticality, from, to, pageable);

        Mockito.verify(feedbackRepository, Mockito.times(1))
                .findAll(Mockito.<Specification<FeedbackEntity>>any(), Mockito.eq(pageable));

        Assertions.assertEquals(page, result);
    }
}
