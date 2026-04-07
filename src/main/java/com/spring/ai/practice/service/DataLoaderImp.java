package com.spring.ai.practice.service;

import com.spring.ai.practice.service.Interface.DataLoader;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IO;
import java.util.List;


@Service
public class DataLoaderImp implements DataLoader {

    @Value("classpath:sample_data.json")
    private Resource jsonResource;

    @Value("classpath:cricket_rules.pdf")
    private Resource pdfResource;


    @Override
    public List<Document> loadDocumentsFromJson() {
        IO.println("Hello print");
        var jsonReader = new JsonReader(jsonResource,"project");
        return jsonReader.read();
    }

    @Override
    public List<Document> loadDocumentFromPdf() {

        var pdfReader = new PagePdfDocumentReader(pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .build()
                );

        return pdfReader.read();
    }
}
