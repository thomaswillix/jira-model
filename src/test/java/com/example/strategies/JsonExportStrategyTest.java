package com.example.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.model.Project;
import com.example.model.Sprint;
import com.example.model.Team;
import com.example.model.User;
import com.example.model.issue.IssueStub;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonExportStrategyTest {

    private static final LocalDate START = LocalDate.of(2025, 1, 1);
    private static final LocalDate END = LocalDate.of(2025, 1, 31);
    private static final Team TEAM = new Team("Alpha", List.of(new User("john_doe")));
    private static final ExportStrategy strategy = new JsonExportStrategy();

    private Project emptyProject() {
        return new Project("PROJ", "My Project", "Description", new ArrayList<>());
    }

    @Test
    public void shouldReturnValidJson() {
        String result = strategy.exportProjectsInformation(emptyProject());

        assertDoesNotThrow(() -> new ObjectMapper().readTree(result));
    }

    @Test
    public void shouldIncludeProjectFieldsInJson() {
        String result = strategy.exportProjectsInformation(emptyProject());

        assertTrue(result.contains("\"projectKey\""));
        assertTrue(result.contains("\"PROJ\""));
        assertTrue(result.contains("\"name\""));
        assertTrue(result.contains("\"My Project\""));
        assertTrue(result.contains("\"description\""));
    }

    @Test
    public void shouldIncludeSprintInformationInJson() {
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1", new ArrayList<>());
        Project project = new Project("PROJ", "My Project", "Description",
                new ArrayList<>(List.of(sprint))
        );

        String result = strategy.exportProjectsInformation(project);

        assertTrue(result.contains("\"sprints\""));
        assertTrue(result.contains("\"Sprint 1\""));
        assertTrue(result.contains("\"2025-01-01\""));
        assertTrue(result.contains("\"2025-01-31\""));
    }

    @Test
    public void shouldIncludeIssueInformationInJson() {
        IssueStub issue = new IssueStub("Fix bug", "A nasty bug", 8, 0.0);
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1",
                new ArrayList<>(List.of(issue))
        );
        Project project = new Project("PROJ", "My Project", "Description",
                new ArrayList<>(List.of(sprint))
        );

        String result = strategy.exportProjectsInformation(project);

        assertTrue(result.contains("\"Fix bug\""));
        assertTrue(result.contains("\"A nasty bug\""));
        assertTrue(result.contains("\"estimatedHours\""));
    }

    @Test
    public void shouldIncludeTeamAndUserInformationInJson() {
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1", new ArrayList<>());
        Project project = new Project("PROJ", "My Project", "Description",
                new ArrayList<>(List.of(sprint))
        );

        String result = strategy.exportProjectsInformation(project);

        assertTrue(result.contains("\"Alpha\""));
        assertTrue(result.contains("\"john_doe\""));
    }

    @Test
    public void shouldSerializeDatesAsStringsNotArrays() {
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1", new ArrayList<>());
        Project project = new Project("PROJ", "My Project", "Description",
                new ArrayList<>(List.of(sprint))
        );

        String result = strategy.exportProjectsInformation(project);

        assertTrue(result.contains("\"2025-01-01\""));
        assertFalse(result.contains("[2025,"));
    }
}