package com.maximys777.Test.task.feedback.service;

import com.maximys777.Test.task.feedback.dto.request.FeedbackRequest;
import com.maximys777.Test.task.feedback.dto.response.FeedbackAnalysisResponse;
import com.maximys777.Test.task.feedback.entity.FeedbackEntity;
import com.maximys777.Test.task.feedback.entity.common.FeedbackType;
import com.maximys777.Test.task.feedback.repository.FeedbackRepository;
import com.maximys777.Test.task.googledocs.service.GoogleDocsService;
import com.maximys777.Test.task.openai.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ChatGPTService chatGPTService;
    private final GoogleDocsService googleDocsService;

    public void createNewFeedback(FeedbackRequest request) {
        FeedbackAnalysisResponse responseAnalysis = chatGPTService.analyzeFeedback(request.getMessage());

        FeedbackEntity entity = FeedbackEntity.builder()
                .role(request.getRole())
                .department(request.getDepartment())
                .message(request.getMessage())
                .feedbackType(responseAnalysis.feedbackType())
                .criticality(responseAnalysis.criticality())
                .suggestion(responseAnalysis.suggestion())
                .build();

        feedbackRepository.save(entity);

        googleDocsService.appendFeedback(entity);
    }

    public Page<FeedbackEntity> getAllByFilter(String role,
                                               String department,
                                               FeedbackType feedbackType,
                                               Integer criticality,
                                               LocalDateTime createdFrom,
                                               LocalDateTime createdTo,
                                               Pageable pageable) {
        Specification<FeedbackEntity> spec = specification(role, department, feedbackType, criticality, createdFrom, createdTo);

        return feedbackRepository.findAll(spec, pageable);
    }

    private Specification<FeedbackEntity> specification(String role,
                                                        String department,
                                                        FeedbackType feedbackType,
                                                        Integer criticality,
                                                        LocalDateTime createdFrom,
                                                        LocalDateTime createdTo) {
        Specification<FeedbackEntity> spec = (root, query, cb) -> cb.conjunction();

        if (role != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("role")), "%" + role.toLowerCase() + "%"));
        }

        if (department != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("department")), "%" + department.toLowerCase() + "%"));
        }

        if (feedbackType != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("feedbackType"), feedbackType));
        }

        if (criticality != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("criticality"), criticality));
        }

        if (createdFrom != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom));
        }

        if (createdTo != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), createdTo));
        }

        return spec;
    }
}
