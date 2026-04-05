package com.spring.ai.practice.service;

import com.spring.ai.practice.service.Interface.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;


@Component
public class ChatServiceImp implements ChatService {

    private final ChatClient chatClient;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("classpath:/prompts/User-message.st")
    private Resource userMessage;

    @Value("classpath:/prompts/System-message.st")
    private Resource systemMessage;

    private VectorStore vectorStore;

    public ChatServiceImp(ChatClient chatClient, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }


    //running this on test and save data into vector db
    @Override
    public void saveData(List<String> list) {

        List<Document> documentList = list.stream().map(Document::new).toList();
        this.vectorStore.add(documentList);


    }
    @Override
    public String ragChat(String query, String userId) {

        //load data from vector data base

        SearchRequest searchRequest = SearchRequest
                .builder()
                .topK(3)
                .similarityThreshold(0.6)
                .query(query)
                .build();

        List<Document> documents = this.vectorStore.similaritySearch(searchRequest);
        List<String> documentList = documents.stream().map(Document::getText).toList();
        String contextData = String.join(" , ", documentList);
        this.logger.info("Context data : {} ",contextData);
        //similar results user query
        //pass in context


        return chatClient
                .prompt()
                .system(system -> system.text(this.systemMessage).param("documents", contextData))
                .user(user -> user.text(this.userMessage).param("query", query))
                .call()
                .content();
    }


    //QuestionAnswerAdvisor
    @Override
    public String ragChatQuestionAnserAdvicsor(String query, String userId) {

        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(
                        SearchRequest.builder()
                                .topK(3)
                                .similarityThreshold(0.6)
                                .build()
                )
                .build();

        return chatClient
                .prompt()
                .advisors(questionAnswerAdvisor)
                .user(user -> user.text(this.userMessage).param("query", query))
                .call()
                .content();
    }
    //RetrievalAugmentationAdvisor
    @Override
    public String ragChatRetrievalAugmentationAdvisor(String query, String userId) {

        var advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)
                                .topK(3)
                                .similarityThreshold(0.5)
                                .build()
                )
                .queryAugmenter(ContextualQueryAugmenter
                        .builder()
                        .allowEmptyContext(true)
//                        .promptTemplate() //if you don't want to use default prompt template
                        .build())
                .build();


        return chatClient
                .prompt()
                .advisors(advisor)
                .user(user -> user.text(this.userMessage).param("query", query))
                .call()
                .content();
    }
}
