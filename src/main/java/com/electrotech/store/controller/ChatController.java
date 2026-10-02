package com.electrotech.store.controller;

import com.electrotech.store.service.ChatService;
import org.springframework.web.bind.annotation.*;
import java.util.Map; // ASTA trebuie să fie singura referință pentru Map

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public Map<String, String > askAI(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        String reply = chatService.getAiResponse(userMessage);
        return Map.of("reply", reply);
    }
}