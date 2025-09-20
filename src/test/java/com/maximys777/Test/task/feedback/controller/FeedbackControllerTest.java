package com.maximys777.Test.task.feedback.controller;

import com.maximys777.Test.task.feedback.entity.FeedbackEntity;
import com.maximys777.Test.task.feedback.entity.common.FeedbackType;
import com.maximys777.Test.task.feedback.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeedbackService feedbackService;

    @Test
    void getAllByFilter_ShouldReturnAllFeedback_WhenSuccess() throws Exception {
        FeedbackEntity entity = FeedbackEntity.builder()
                .id(1L)
                .role("Manager")
                .department("IT")
                .message("Test message")
                .feedbackType(FeedbackType.NEUTRAL)
                .criticality(2)
                .build();

        Page<FeedbackEntity> page = new PageImpl<>(List.of(entity));

        Mockito.when(feedbackService.getAllByFilter(
                anyString(),
                anyString(),
                any(),
                any(),
                any(),
                any(),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/feedbacks")
                        .param("role", "Manager")
                        .param("department", "IT")
                        .param("criticality", "5")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        Mockito.verify(feedbackService, Mockito.times(1))
                .getAllByFilter(
                        eq("Manager"),
                        eq("IT"),
                        isNull(),
                        eq(5),
                        isNull(),
                        isNull(),
                        any(Pageable.class)
                );
    }
}
