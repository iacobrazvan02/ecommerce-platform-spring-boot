package com.electrotech.store.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;
import java.util.List;

@Service
public class ChatService {

    private final String geminiUrl;
    private final WebClient webClient;

    public ChatService(WebClient.Builder webClientBuilder,
                       @Value("${gemini.api.key}") String apiKey) {
        this.geminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        this.webClient = webClientBuilder.build();
    }

    public String getAiResponse(String userMessage) {
        String systemPrompt = "Ești asistentul virtual politicos al magazinului ElectroTech. " +
                "Ajuți clienții cu informații despre produse electronice. " +
                "Răspunde prietenos și scurt în limba română.";

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", systemPrompt + "\n\nClient: " + userMessage)
                        ))
                )
        );

        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                Map<?, ?> response = webClient.post()
                        .uri(geminiUrl)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

                if (response == null) return "Eroare: Serverul nu a răspuns.";

                List<?> candidates = (List<?>) response.get("candidates");
                if (candidates == null || candidates.isEmpty()) return "AI-ul nu a putut genera un răspuns.";

                Map<?, ?> firstCandidate = (Map<?, ?>) candidates.get(0);
                Map<?, ?> content = (Map<?, ?>) firstCandidate.get("content");
                List<?> parts = (List<?>) content.get("parts");
                Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);

                return (String) firstPart.get("text");

            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("429") && i < maxRetries - 1) {
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.err.println("Eroare ChatService: " + e.getMessage());
                    return "Ne pare rău, asistentul are o problemă tehnică: " + e.getMessage();
                }
            }
        }
        return "Ne pare rău, asistentul este ocupat. Încearcă din nou în câteva secunde.";
    }
}