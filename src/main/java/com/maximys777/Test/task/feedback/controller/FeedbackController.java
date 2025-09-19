package com.maximys777.Test.task.feedback.controller;

import com.maximys777.Test.task.feedback.entity.FeedbackEntity;
import com.maximys777.Test.task.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping
    public Page<FeedbackEntity> getAllByFilter(@RequestParam(required = false) String role,
                                               @RequestParam(required = false) String department,
                                               @RequestParam(required = false) String feedbackType,
                                               @RequestParam(required = false) Integer criticality,
                                               @RequestParam(required = false) LocalDateTime createdFrom,
                                               @RequestParam(required = false) LocalDateTime createdTo,
                                               Pageable pageable) {
        return feedbackService.getAllByFilter(role, department, feedbackType, criticality, createdFrom, createdTo, pageable);
    }
}
