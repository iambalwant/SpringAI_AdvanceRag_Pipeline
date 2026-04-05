package com.spring.ai.practice.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/multiModel")
public class ChatController {

    private ChatClient openAiChatClinet;

    private ChatClient ollamaAiChatClinet;

    public ChatController(@Qualifier("openAiChatClient") ChatClient openAiChatClinet,
                          @Qualifier("ollamaAiChatClient") ChatClient ollamaAiChatClinet){
        this.openAiChatClinet = openAiChatClinet;
        this.ollamaAiChatClinet = ollamaAiChatClinet;
    }

// Because we created a Chatclient config file to handle this
//    public ChatController(
//            OpenAiChatModel openAiChatModel,
//            OllamaChatModel ollamaChatModel
//    ){
//        this.openAiChatClinet=ChatClient.builder(openAiChatModel).build();
//        this.ollamaAiChatClinet=ChatClient.builder(ollamaChatModel).build();
//    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam(
                    value = "q",
                    required = true
            )
            String q
    ){
           var resultResponse = this.ollamaAiChatClinet
                   .prompt(q)
                   .call()
                   .content();
           return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
