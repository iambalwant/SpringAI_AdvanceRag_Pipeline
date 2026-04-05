package com.spring.ai.practice.controller;


import com.spring.ai.practice.service.Interface.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/multiModel")
public class ChatController {


    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/rag-chat")
    public ResponseEntity<String> ragChat(
            @RequestParam(
                    value = "q",
                    required = true
            ) String q
    ){
        return new ResponseEntity<>(chatService.ragChat(q), HttpStatus.OK);

    }
}
