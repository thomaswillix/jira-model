package com.example.strategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.model.Project;

public class JsonExportStrategy implements ExportStrategy{

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public String exportProjectsInformation(Project project) {
        try {
            String resultJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(project);
            System.out.println(resultJson);
            return resultJson;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error exporting project to JSON", e);
        }
    }

}
