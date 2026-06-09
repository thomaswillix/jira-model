package com.example.strategies;

import com.example.model.Project;

public class ToStringExportStrategy implements ExportStrategy{
    @Override
    public String exportProjectsInformation(Project project) {
        return project.toString();
    }
}
