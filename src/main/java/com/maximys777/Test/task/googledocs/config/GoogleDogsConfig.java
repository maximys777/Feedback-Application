package com.maximys777.Test.task.googledocs.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Configuration
public class GoogleDogsConfig {

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${google.service.account.key-file}")
    private String googleKey;

    @Value("${google.app.name}")
    private String appName;

    @Bean
    public Docs googleDocs() throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        try (InputStream in = new FileInputStream(googleKey)) {
            GoogleCredential credential = GoogleCredential.fromStream(in);

            if (credential.createScopedRequired()) {
                credential = credential.createScoped(List.of(
                        DocsScopes.DOCUMENTS
                ));
            }

            return new Docs.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(appName)
                    .build();
        }
    }
}
