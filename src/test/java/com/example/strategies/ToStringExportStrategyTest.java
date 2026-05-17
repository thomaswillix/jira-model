package com.example.strategies;

import com.example.model.Project;
import com.example.model.Sprint;
import com.example.model.Team;
import com.example.model.User;
import com.example.model.issue.IssueStub;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ToStringExportStrategyTest {

    private static final LocalDate START = LocalDate.of(2025, 1, 1);
    private static final LocalDate END = LocalDate.of(2025, 1, 31);
    private static final Team TEAM = new Team("Alpha", List.of(new User("john_doe")));
    private static final ExportStrategy strategy = new ToStringExportStrategy();

    private Project emptyProject() {
        return new Project("PROJ", "My Project", "Description", new ArrayList<>());
    }

    @Test
    public void shouldReturnStringRepresentationOfProject() {
        Project project = emptyProject();

        String result = strategy.exportProjectsInformation(project);

        assertTrue(result.contains("PROJ"));
        assertTrue(result.contains("My Project"));
        assertTrue(result.contains("Description"));
    }

    @Test
    public void shouldIncludeSprintInformation() {
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1", new ArrayList<>());
        Project project = new Project("PROJ", "My Project", "Description",
                new ArrayList<>(List.of(sprint))
        );

        String result = strategy.exportProjectsInformation(project);

        assertTrue(result.contains("Sprint 1"));
        assertTrue(result.contains("2025-01-01"));
        assertTrue(result.contains("2025-01-31"));
    }

    @Test
    public void shouldIncludeIssueInformation() {
        IssueStub issue = new IssueStub("Fix bug", "A nasty bug", 8, 0.0);
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1",
                new ArrayList<>(List.of(issue))
        );
        Project project = new Project("PROJ", "My Project", "Description",
                new ArrayList<>(List.of(sprint))
        );

        String result = strategy.exportProjectsInformation(project);

        assertTrue(result.contains("Fix bug"));
        assertTrue(result.contains("A nasty bug"));
    }

    @Test
    public void shouldIncludeTeamAndUserInformation() {
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1", new ArrayList<>());
        Project project = new Project("PROJ", "My Project", "Description",
                new ArrayList<>(List.of(sprint))
        );

        String result = strategy.exportProjectsInformation(project);

        assertTrue(result.contains("Alpha"));
        assertTrue(result.contains("john_doe"));
    }
}
