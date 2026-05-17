package com.example.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

    private static final LocalDate JAN_1 = LocalDate.of(2025, 1, 1);
    private static final LocalDate JAN_31 = LocalDate.of(2025, 1, 31);
    private static final LocalDate FEB_1 = LocalDate.of(2025, 2, 1);
    private static final LocalDate FEB_28 = LocalDate.of(2025, 2, 28);
    private static final Team TEAM = new Team("Alpha", List.of(new User("john_doe")));

    private Project emptyProject() {
        return new Project("PROJ", "Project", "Description", new ArrayList<>());
    }

    private Project projectWithSprint(Sprint sprint) {
        return new Project("PROJ", "Project", "Description", new ArrayList<>(List.of(sprint)));
    }

    // -------------------------------------------------------------------------
    // Constructor - happy path
    // -------------------------------------------------------------------------

    @Test
    public void shouldCreateValidProject() {
        Project project = emptyProject();

        assertEquals("PROJ", project.getProjectKey());
        assertEquals("Project", project.getName());
        assertEquals("Description", project.getDescription());
        assertTrue(project.getSprints().isEmpty());
    }

    // -------------------------------------------------------------------------
    // Constructor - projectKey
    // -------------------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(strings = {"A", "LONGERKEY", "ABCDE"})
    public void shouldNotAllowProjectKeysOutsideLengthLimits(String invalidLengthKey) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Project(invalidLengthKey, "Project", "Test Project", new ArrayList<>())
        );
        assertEquals("Project key length must be between 2 and 4 characters.", exception.getMessage());
    }

    @Test
    public void shouldForceProjectKeyToUpperCaseAndTrimSpaces() {
        Project project = new Project("  proj  ", "Project", "Test Project", new ArrayList<>());
        assertEquals("PROJ", project.getProjectKey());
    }

    @Test
    public void shouldNotAllowNullProjectKey() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Project(null, "Project", "Test Project", new ArrayList<>())
        );
        assertEquals("Project key can't be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    public void shouldNotAllowEmptyOrBlankProjectKeys(String invalidKey) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Project(invalidKey, "Project", "Test Project", new ArrayList<>())
        );
        assertEquals("Project key can't be blank.", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Constructor - name
    // -------------------------------------------------------------------------

    @Test
    public void shouldNotAllowNullProjectName() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Project("PROJ", null, "Test Project", new ArrayList<>())
        );
        assertEquals("Project name can't be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    public void shouldNotAllowEmptyOrBlankProjectNames(String invalidName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Project("PROJ", invalidName, "Test Project", new ArrayList<>())
        );
        assertEquals("Project name can't be blank.", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Constructor - description
    // -------------------------------------------------------------------------

    @Test
    public void shouldNotAllowNullProjectDescription() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Project("PROJ", "Project", null, new ArrayList<>())
        );
        assertEquals("Project description can't be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    public void shouldNotAllowEmptyOrBlankProjectDescriptions(String invalidDescription) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Project("PROJ", "Project", invalidDescription, new ArrayList<>())
        );
        assertEquals("Project description can't be blank.", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Constructor - sprints
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionWhenSprintsListIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Project("PROJ", "Project", "Description", null)
        );
    }

    @Test
    public void shouldNotModifyOriginalSprintsListWhenProjectIsCreated() {
        Sprint sprint = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());
        List<Sprint> originalList = new ArrayList<>(List.of(sprint));
        Project project = new Project("PROJ", "Project", "Description", originalList);

        originalList.add(new Sprint(FEB_1, FEB_28, TEAM, "Sprint febrero", new ArrayList<>()));

        assertEquals(1, project.getSprints().size());
    }

    // -------------------------------------------------------------------------
    // setDescription
    // -------------------------------------------------------------------------

    @Test
    public void shouldAllowUpdatingDescription() {
        Project project = emptyProject();

        project.setDescription("New description");

        assertEquals("New description", project.getDescription());
    }

    @Test
    public void shouldNotAllowSettingNullDescription() {
        Project project = emptyProject();

        assertThrows(NullPointerException.class, () -> project.setDescription(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    public void shouldNotAllowSettingBlankDescription(String blank) {
        Project project = emptyProject();

        assertThrows(IllegalArgumentException.class, () -> project.setDescription(blank));
    }

    // -------------------------------------------------------------------------
    // getSprints - inmutabilidad
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionIfTryingToModifySprintsListExternally() {
        Project project = emptyProject();
        Sprint sprint = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());

        assertThrows(UnsupportedOperationException.class, () ->
                project.getSprints().add(sprint)
        );
    }

    // -------------------------------------------------------------------------
    // addSprint - happy path
    // -------------------------------------------------------------------------

    @Test
    public void shouldAddNonOverlappingSprint() {
        Sprint jan = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());
        Sprint feb = new Sprint(FEB_1, FEB_28, TEAM, "Sprint febrero", new ArrayList<>());
        Project project = projectWithSprint(jan);

        project.addSprint(feb);

        assertEquals(2, project.getSprints().size());
        assertTrue(project.getSprints().contains(feb));
    }

    // -------------------------------------------------------------------------
    // addSprint - validaciones
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionWhenAddingNullSprint() {
        assertThrows(NullPointerException.class, () -> emptyProject().addSprint(null));
    }

    @Test
    public void shouldThrowExceptionWhenAddingDuplicateSprint() {
        Sprint sprint = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());
        Project project = projectWithSprint(sprint);

        assertThrows(IllegalArgumentException.class, () -> project.addSprint(sprint));
    }

    @Test
    public void shouldThrowExceptionWhenStartDateOverlaps() {
        Sprint existing = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());
        Sprint overlapping = new Sprint(
                LocalDate.of(2025, 1, 15), FEB_28,
                TEAM, "Overlapping", new ArrayList<>()
        );
        Project project = projectWithSprint(existing);

        assertThrows(IllegalArgumentException.class, () -> project.addSprint(overlapping));
    }

    @Test
    public void shouldThrowExceptionWhenEndDateOverlaps() {
        Sprint existing = new Sprint(FEB_1, FEB_28, TEAM, "Sprint febrero", new ArrayList<>());
        Sprint overlapping = new Sprint(
                JAN_1, LocalDate.of(2025, 2, 15),
                TEAM, "Overlapping", new ArrayList<>()
        );
        Project project = projectWithSprint(existing);

        assertThrows(IllegalArgumentException.class, () -> project.addSprint(overlapping));
    }

    @Test
    public void shouldThrowExceptionWhenNewSprintContainsExisting() {
        Sprint existing = new Sprint(LocalDate.of(2025, 1, 10),
                LocalDate.of(2025, 1, 20), TEAM, "Inner", new ArrayList<>());
        Sprint overlapping = new Sprint(JAN_1, JAN_31, TEAM, "Outer", new ArrayList<>());
        Project project = projectWithSprint(existing);

        assertThrows(IllegalArgumentException.class, () -> project.addSprint(overlapping));
    }

    // -------------------------------------------------------------------------
    // removeSprint - happy path
    // -------------------------------------------------------------------------

    @Test
    public void shouldRemoveExistingSprint() {
        Sprint sprint = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());
        Project project = projectWithSprint(sprint);

        project.removeSprint(sprint);

        assertTrue(project.getSprints().isEmpty());
    }

    // -------------------------------------------------------------------------
    // removeSprint - validaciones
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionWhenRemovingNullSprint() {
        assertThrows(NullPointerException.class, () -> emptyProject().removeSprint(null));
    }

    @Test
    public void shouldThrowExceptionWhenRemovingSprintNotInProject() {
        Sprint sprint = new Sprint(JAN_1, JAN_31, TEAM, "Sprint enero", new ArrayList<>());
        Sprint stranger = new Sprint(FEB_1, FEB_28, TEAM, "Sprint febrero", new ArrayList<>());
        Project project = projectWithSprint(sprint);

        assertThrows(IllegalArgumentException.class, () -> project.removeSprint(stranger));
    }
}
