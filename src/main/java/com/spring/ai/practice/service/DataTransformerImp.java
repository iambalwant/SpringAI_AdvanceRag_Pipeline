package com.spring.ai.practice.service;


import com.spring.ai.practice.service.Interface.DataTransformerService;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTransformerImp implements DataTransformerService {


    @Override
    public List<Document> transform(List<Document> documents) {

        var splitter = new TokenTextSplitter();
        return splitter.transform(documents);

    }
}
