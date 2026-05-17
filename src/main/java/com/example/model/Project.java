package com.example.model;

import com.example.functions.DateHelper;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.functions.Validation.*;

@Getter
public class Project {

    private static final int MAX_KEY_LENGTH = 4;
    private static final int MIN_KEY_LENGTH = 2;

    private final String projectKey;
    private final String name;
    private String description;
    private final List<Sprint> sprints;

    public Project (String projectKey, String name, String description, List<Sprint> sprints){
        requireNonNull(sprints, "Sprint list");

        this.projectKey = requireValidProjectKey(projectKey);
        this.name = requireValidString(name, "Project name");
        this.description = requireValidString(description, "Project description");
        this.sprints = new ArrayList<>(sprints);
    }

    public List<Sprint> getSprints() {
        return Collections.unmodifiableList(this.sprints);
    }

    public void setDescription(String description) {
        this.description = requireValidString(description, "Project description");
    }

    public void addSprint(Sprint sprint) {
        requireNonNull(sprint, "Sprint");
        if (this.sprints.contains(sprint))
            throw new IllegalArgumentException("Sprint is already linked to this project.");

        if (isSprintOverlapping(sprint))
            throw new IllegalArgumentException("Sprint dates overlap others added into this project");

        this.sprints.add(sprint);
    }

    public void removeSprint(Sprint sprint) {
        requireNonNull(sprint, "Sprint");

        if (!this.sprints.remove(sprint))
            throw new IllegalArgumentException("Sprint is not linked to this project.");
    }

    private boolean isSprintOverlapping(Sprint sprint) {
        return sprints.stream().anyMatch(s ->
                DateHelper.isBetween(sprint.getStartDate(), s.getStartDate(), s.getEndDate()) ||
                DateHelper.isBetween(sprint.getEndDate(), s.getStartDate(), s.getEndDate()) ||
                DateHelper.isContainedInRange(sprint.getStartDate(), sprint.getEndDate(), s.getStartDate(), s.getEndDate())
        );
    }

    private String requireValidProjectKey(String projectKey) {
        requireNotBlank(projectKey, "Project key");
        projectKey = projectKey.trim().toUpperCase();

        if (projectKey.length() < MIN_KEY_LENGTH || projectKey.length() > MAX_KEY_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Project key length must be between %d and %d characters.",
                            MIN_KEY_LENGTH,
                            MAX_KEY_LENGTH
                    )
            );
        }
        return projectKey;
    }

    @Override
    public String toString() {
        String sprintsStr = sprints.stream()
                .map(Sprint::toString)
                .collect(Collectors.joining("\n"));

        return "Project{" +
                "projectKey='" + projectKey + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sprints=" + sprintsStr +
                '}';
    }
}
