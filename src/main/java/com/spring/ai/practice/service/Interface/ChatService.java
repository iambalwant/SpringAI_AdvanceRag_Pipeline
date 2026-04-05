package com.spring.ai.practice.service.Interface;

import com.spring.ai.practice.entity.Tut;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {
    void saveData(List<String> list);
    String ragChat(String query, String userId);
    String ragChatQuestionAnserAdvicsor(String query, String userId);
    String ragChatRetrievalAugmentationAdvisor(String query, String userId);
}
