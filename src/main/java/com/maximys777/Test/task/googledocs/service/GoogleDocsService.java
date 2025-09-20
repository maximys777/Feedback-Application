package com.maximys777.Test.task.googledocs.service;

import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.BatchUpdateDocumentRequest;
import com.google.api.services.docs.v1.model.EndOfSegmentLocation;
import com.google.api.services.docs.v1.model.InsertTextRequest;
import com.google.api.services.docs.v1.model.Request;
import com.maximys777.Test.task.feedback.entity.FeedbackEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleDocsService {

    private final Docs googleDocs;

    @Value("${google.docs.document-id}")
    private String documentId;

    public void appendFeedback(FeedbackEntity entity) {
        try {
            String text = """
                    --- Відгук ---
                    Посада: %s
                    Філія: %s
                    Вігук:: %s
                    Тип відгуку: %s
                    Критичність: %d
                    Рекомендація від AI: %s
                    ----------------
                    """.formatted(
                    entity.getRole(),
                    entity.getDepartment(),
                    entity.getMessage(),
                    entity.getFeedbackType(),
                    entity.getCriticality(),
                    entity.getSuggestion()
            );

            List<Request> requests = List.of(
                    new Request().setInsertText(
                            new InsertTextRequest()
                                    .setText(text + "\n")
                                    .setEndOfSegmentLocation(new EndOfSegmentLocation())
                    )
            );

            BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);
            googleDocs.documents().batchUpdate(documentId, body).execute();
        } catch (IOException e) {
            throw new RuntimeException("Не вдалося добавити відгук в Google Docs ", e);
        }
    }

}