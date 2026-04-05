package com.spring.ai.practice.service.Interface;

import java.util.List;

public interface ChatService {
    void saveData(List<String> list);
    String ragChat(String query);
}
