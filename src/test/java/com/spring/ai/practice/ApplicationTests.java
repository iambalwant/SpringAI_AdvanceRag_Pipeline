package com.spring.ai.practice;

import com.spring.ai.practice.helper.Helper;
import com.spring.ai.practice.service.Interface.ChatService;
import com.spring.ai.practice.service.Interface.DataLoader;
import com.spring.ai.practice.service.Interface.DataTransformerService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IO;
import java.util.List;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private ChatService chatService;

	@Autowired
	private DataLoader dataLoader;

	@Autowired
	private DataTransformerService dataTransformerService;

	@Autowired
	private VectorStore vectorStore;

	@Test
    void saveDataToVectorDatabase(){
		System.out.println("saving data to database");
		this.chatService.saveData(Helper.getData());
		System.out.println("saving data to database done");
	}

	@Test
	void dataLoaderJson(){
		System.out.println("loading data");
		var documents = this.dataLoader.loadDocumentsFromJson();
		IO.println(documents.size());

		documents.forEach(item->{
			IO.println(item);
		});

		System.out.println("loading data done");
	}

	@Test
	void dataLoaderPdf(){
		List<Document> documents = this.dataLoader.loadDocumentFromPdf();
		System.out.println(documents.size());
		documents.forEach(item->{
			System.out.println(item);
			System.out.println("-------------------------");
		});
		
		IO.println("Readed going to tranform");

		List<Document> transformDocument = this.dataTransformerService.transform(documents);
		System.out.println(transformDocument);

		//going to save the data into database

		this.vectorStore.add(transformDocument);
		System.out.println("done");
	}
	
	

}
