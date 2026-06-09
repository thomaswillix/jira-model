package com.example.service;

import com.example.model.Project;
import com.example.model.Sprint;
import com.example.model.User;
import com.example.repository.ProjectRepository;
import com.example.strategies.JsonExportStrategy;
import com.example.strategies.ToStringExportStrategy;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.example.functions.Validation.requireNonNull;
import static com.example.functions.Validation.requireNotBlank;

public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Optional<Project> getProjectByProjectKey(String projectKey){
        requireNotBlank(projectKey, "Project key");
        return projectRepository.findByProjectKey(projectKey);
    }

    public List<Sprint> getSprintsByProject(Project project){
        requireNonNull(project, "Project");
        return projectRepository.findAllSprintsByProject(project);
    }

    public boolean hasActiveSprints(Project project){
        requireNonNull(project, "Project");
        return project.getSprints().stream().anyMatch(s ->
                !s.getStartDate().isAfter(LocalDate.now()) &&
                !s.getEndDate().isBefore(LocalDate.now())
        );
    }

    public List<Sprint> getSprintsSortedByStartDate(Project project){
        requireNonNull(project, "Project");
        return project.getSprints().stream().sorted(Comparator.comparing(Sprint::getStartDate)).toList();
    }

    public long countTotalIssues(Project project){
        requireNonNull(project, "Project");
        return project.getSprints().stream()
                .mapToLong(sprint -> sprint.getIssues().size())
                .sum();
    }

    public Optional<Sprint> getLongestSprint(Project project){
        requireNonNull(project, "Project");
        return project.getSprints().stream().max(Comparator.comparing(Sprint::calculateTotalEstimatedHours));
    }

    public List<Sprint> getSprintsByUser(Project project, User user){
        requireNonNull(project, "Project");
        requireNonNull(user, "User");
        return project.getSprints().stream()
                .filter(sprint -> sprint.getTeam().getUsers().contains(user))
                .toList();
    }

    public List<Project> getProjectsWithActiveSprints(List<Project> projects){
        requireNonNull(projects, "Projects list");
        return projects.stream()
                .filter(this::hasActiveSprints)
                .toList();
    }

    public String exportProjectDataToString(Project project){
        requireNonNull(project, "Project");
        return new ToStringExportStrategy().exportProjectsInformation(project);
    }

    public String exportProjectDataToJson(Project project){
        requireNonNull(project, "Project");
        return new JsonExportStrategy().exportProjectsInformation(project);
    }
}
