package com.maximys777.Test.task.feedback.service;

import com.maximys777.Test.task.feedback.dto.request.FeedbackRequest;
import com.maximys777.Test.task.feedback.entity.FeedbackEntity;
import com.maximys777.Test.task.feedback.entity.common.FeedbackType;
import com.maximys777.Test.task.feedback.repository.FeedbackRepository;
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

    public void createNewFeedback(FeedbackRequest request) {
        FeedbackEntity entity = FeedbackEntity.builder()
                .role(request.getRole())
                .department(request.getDepartment())
                .message(request.getMessage())
                .feedbackType(FeedbackType.WISH)
                .criticality(5)
                .build();

        feedbackRepository.save(entity);
    }

    public Page<FeedbackEntity> getAllByFilter(String role,
                                               String department,
                                               String feedbackType,
                                               Integer criticality,
                                               LocalDateTime createdFrom,
                                               LocalDateTime createdTo,
                                               Pageable pageable) {
        Specification<FeedbackEntity> spec = specification(role, department, feedbackType, criticality, createdFrom, createdTo);

        return feedbackRepository.findAll(spec, pageable);
    }

    private Specification<FeedbackEntity> specification(String role,
                                                        String department,
                                                        String feedbackType,
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
                    cb.like(cb.lower(root.get("feedbackType")), "%" + feedbackType.toLowerCase() + "%"));
        }

        if (criticality != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("criticality"), criticality));
        }

        if (createdFrom != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("created"), createdFrom));
        }

        if (createdTo != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("created"), createdTo));
        }

        return spec;
    }
}
