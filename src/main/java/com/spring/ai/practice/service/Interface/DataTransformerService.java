package com.spring.ai.practice.service.Interface;

import org.springframework.ai.document.Document;

import java.util.List;

public interface DataTransformerService {

    List<Document> transform(List<Document> documents);

}
