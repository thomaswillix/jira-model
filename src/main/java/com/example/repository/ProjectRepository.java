package com.example.repository;

import com.example.model.Project;
import com.example.model.Sprint;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Optional<Project> findByProjectKey(String projectKey);
    List<Sprint> findAllSprintsByProject(Project project);
}
