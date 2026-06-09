package com.example.service;

import com.example.model.Project;
import com.example.model.Sprint;
import com.example.model.Team;
import com.example.model.User;
import com.example.model.issue.IssueStub;
import com.example.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    private static final LocalDate JAN_1 = LocalDate.of(2025, 1, 1);
    private static final LocalDate JAN_31 = LocalDate.of(2025, 1, 31);
    private static final LocalDate FEB_1 = LocalDate.of(2025, 2, 1);
    private static final LocalDate FEB_28 = LocalDate.of(2025, 2, 28);
    private static final Team TEAM = new Team("Alpha", List.of(new User("john_doe")));

    @Mock
    ProjectRepository projectRepository;

    ProjectService projectService;

    @BeforeEach
    void init() {
        projectService = new ProjectService(projectRepository);
    }

    private Project emptyProject() {
        return new Project("PROJ", "My Project", "Description", new ArrayList<>());
    }

    private Project projectWithSprints() {
        Sprint jan = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());
        Sprint feb = new Sprint(FEB_1, FEB_28, TEAM, "Sprint febrero", new ArrayList<>());
        return new Project("PROJ", "My Project", "Description", new ArrayList<>(List.of(jan, feb)));
    }

    // getProjectByProjectKey

    @Test
    void whenValidKeyAndProjectExists_shouldReturnProject() {
        Project project = emptyProject();
        when(projectRepository.findByProjectKey("PROJ")).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectByProjectKey("PROJ");

        assertTrue(result.isPresent());
        assertEquals("PROJ", result.get().getProjectKey());
    }

    @Test
    void whenValidKeyAndProjectDoesNotExist_shouldReturnEmpty() {
        when(projectRepository.findByProjectKey("NONE")).thenReturn(Optional.empty());

        Optional<Project> result = projectService.getProjectByProjectKey("NONE");

        assertTrue(result.isEmpty());
    }

    @Test
    void whenProjectKeyIsNull_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                projectService.getProjectByProjectKey(null));
    }

    // getSprintsByProject

    @Test
    void whenProjectHasSprints_shouldReturnSprints() {
        Project project = projectWithSprints();
        when(projectRepository.findAllSprintsByProject(project))
                .thenReturn(project.getSprints());

        List<Sprint> result = projectService.getSprintsByProject(project);

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void whenProjectHasNoSprints_getSprintsByProjectShouldReturnEmptyList() {
        Project project = emptyProject();
        when(projectRepository.findAllSprintsByProject(project)).thenReturn(List.of());

        List<Sprint> result = projectService.getSprintsByProject(project);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenProjectIsNullForSprints_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                projectService.getSprintsByProject(null));
    }

    // exportProjectDataToString

    @Test
    void whenValidProject_shouldReturnStringContainingProjectData() {
        Project project = emptyProject();

        String result = projectService.exportProjectDataToString(project);

        assertTrue(result.contains("PROJ"));
        assertTrue(result.contains("My Project"));
        assertTrue(result.contains("Description"));
    }

    @Test
    void whenProjectWithSprints_shouldReturnStringContainingSprintData() {
        Project project = projectWithSprints();

        String result = projectService.exportProjectDataToString(project);

        assertTrue(result.contains("Sprint enero"));
        assertTrue(result.contains("Sprint febrero"));
    }

    @Test
    void whenProjectIsNullForStringExport_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                projectService.exportProjectDataToString(null));
    }

    // exportProjectDataToJson

    @Test
    void whenValidProject_shouldReturnValidJson() {
        Project project = emptyProject();

        String result = projectService.exportProjectDataToJson(project);

        assertTrue(result.contains("\"projectKey\""));
        assertTrue(result.contains("\"PROJ\""));
        assertTrue(result.contains("\"My Project\""));
    }

    @Test
    void whenProjectWithSprints_shouldReturnJsonContainingSprintData() {
        Project project = projectWithSprints();

        String result = projectService.exportProjectDataToJson(project);

        assertTrue(result.contains("\"Sprint enero\""));
        assertTrue(result.contains("\"Sprint febrero\""));
    }

    @Test
    void whenProjectIsNullForJsonExport_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                projectService.exportProjectDataToJson(null));
    }

    // getSprintsSortedByStartDate

    @Test
    void whenProjectHasSprints_shouldReturnSprintsSortedByStartDate() {
        Sprint feb = new Sprint(FEB_1, FEB_28, TEAM, "Sprint febrero", new ArrayList<>());
        Sprint jan = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());
        Project project = new Project("PROJ", "My Project", "Description", new ArrayList<>(List.of(feb, jan)));

        List<Sprint> result = projectService.getSprintsSortedByStartDate(project);

        assertEquals(jan, result.get(0));
        assertEquals(feb, result.get(1));
    }

    @Test
    void whenProjectHasNoSprints_getSprintsSortedByStartDateShouldReturnEmptyList() {
        List<Sprint> result = projectService.getSprintsSortedByStartDate(emptyProject());

        assertTrue(result.isEmpty());
    }

    // countTotalIssues

    @Test
    void whenSprintsHaveIssues_shouldReturnTotalCount() {
        IssueStub issue1 = new IssueStub("Fix bug", "A nasty bug", 8, 0.0);
        IssueStub issue2 = new IssueStub("Add feature", "New feature", 4, 0.0);
        IssueStub issue3 = new IssueStub("Refactor", "Clean up", 2, 0.0);
        Sprint jan = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>(List.of(issue1, issue2)));
        Sprint feb = new Sprint(FEB_1, FEB_28, TEAM, "Sprint febrero", new ArrayList<>(List.of(issue3)));
        Project project = new Project("PROJ", "My Project", "Description", new ArrayList<>(List.of(jan, feb)));

        long result = projectService.countTotalIssues(project);

        assertEquals(3L, result);
    }

    @Test
    void whenNoIssues_shouldReturnZero() {
        long result = projectService.countTotalIssues(projectWithSprints());

        assertEquals(0L, result);
    }

    @Test
    void whenNoSprints_shouldReturnZero() {
        long result = projectService.countTotalIssues(emptyProject());

        assertEquals(0L, result);
    }

    // getLongestSprint

    @Test
    void whenProjectHasSprints_shouldReturnLongestSprint() {
        Sprint jan = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());
        Sprint feb = new Sprint(FEB_1, FEB_28, TEAM, "Sprint febrero", new ArrayList<>());
        Project project = new Project("PROJ", "My Project", "Description", new ArrayList<>(List.of(jan, feb)));

        Optional<Sprint> result = projectService.getLongestSprint(project);

        assertTrue(result.isPresent());
        assertEquals(jan, result.get());
    }

    // getSprintsByUser

    @Test
    void whenUserIsInSprints_shouldReturnMatchingSprints() {
        User john = new User("john_doe");
        Team teamWithJohn = new Team("Alpha", List.of(john));
        Team otherTeam = new Team("Beta", List.of(new User("other_user")));
        Sprint jan = new Sprint(JAN_1, JAN_31, teamWithJohn, "Sprint enero", new ArrayList<>());
        Sprint feb = new Sprint(FEB_1, FEB_28, otherTeam, "Sprint febrero", new ArrayList<>());
        Project project = new Project("PROJ", "My Project", "Description", new ArrayList<>(List.of(jan, feb)));

        List<Sprint> result = projectService.getSprintsByUser(project, john);

        assertEquals(1, result.size());
        assertTrue(result.contains(jan));
    }

    @Test
    void whenUserIsNotInAnySprint_shouldReturnEmptyList() {
        User stranger = new User("stranger");
        List<Sprint> result = projectService.getSprintsByUser(projectWithSprints(), stranger);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenProjectIsNullForGetSprintsByUser_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                projectService.getSprintsByUser(null, new User("john_doe")));
    }

    @Test
    void whenUserIsNull_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                projectService.getSprintsByUser(emptyProject(), null));
    }

    // getProjectsWithActiveSprints

    @Test
    void whenSomeProjectsHaveActiveSprints_shouldReturnOnlyThose() {
        Sprint activeSprint = new Sprint(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                TEAM, "Active", new ArrayList<>()
        );
        Sprint pastSprint = new Sprint(JAN_1, JAN_31, TEAM, "Past", new ArrayList<>());
        Project activeProject = new Project("ACTI", "Active Project", "Description",
                new ArrayList<>(List.of(activeSprint)));
        Project pastProject = new Project("PAST", "Past Project", "Description",
                new ArrayList<>(List.of(pastSprint)));

        List<Project> result = projectService.getProjectsWithActiveSprints(List.of(activeProject, pastProject));

        assertEquals(1, result.size());
        assertTrue(result.contains(activeProject));
    }

    @Test
    void whenNoProjectsHaveActiveSprints_shouldReturnEmptyList() {
        Project pastProject = new Project("PAST", "Past Project", "Description",
                new ArrayList<>(List.of(new Sprint(JAN_1, JAN_31, TEAM, "Past", new ArrayList<>()))));

        List<Project> result = projectService.getProjectsWithActiveSprints(List.of(pastProject));

        assertTrue(result.isEmpty());
    }

    @Test
    void whenProjectsListIsNull_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                projectService.getProjectsWithActiveSprints(null));
    }
}
