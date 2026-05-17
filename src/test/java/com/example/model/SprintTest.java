package com.example.model;

import com.example.model.issue.Issue;
import com.example.model.issue.IssueStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class SprintTest {

    private static final LocalDate START = LocalDate.of(2025, 1, 1);
    private static final LocalDate END = LocalDate.of(2025, 1, 31);
    private static final Team TEAM = new Team("Alpha", List.of(new User("john_doe")));

    private Sprint emptySprint() {
        return new Sprint(START, END, TEAM, "Sprint 1", new ArrayList<>());
    }

    private IssueStub issue(int estimatedHours) {
        return new IssueStub("Title", "Description", estimatedHours, 0.0);
    }

    @Test
    public void shouldCreateValidSprint() {
        Sprint sprint = emptySprint();

        assertEquals(START, sprint.getStartDate());
        assertEquals(END, sprint.getEndDate());
        assertEquals(TEAM, sprint.getTeam());
        assertEquals("Sprint 1", sprint.getDescription());
        assertTrue(sprint.getIssues().isEmpty());
    }

    @Test
    public void shouldNotModifyOriginalIssuesListWhenSprintIsCreated() {
        IssueStub issue = issue(8);
        List<Issue> originalList = new ArrayList<>(List.of(issue));
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1", originalList);

        originalList.add(issue(4));

        assertEquals(1, sprint.getIssues().size());
    }

    // -------------------------------------------------------------------------
    // Constructor - dates
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionWhenStartDateIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Sprint(null, END, TEAM, "Sprint 1", new ArrayList<>())
        );
        assertEquals("Dates cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEndDateIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Sprint(START, null, TEAM, "Sprint 1", new ArrayList<>())
        );
        assertEquals("Dates cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Sprint(END, START, TEAM, "Sprint 1", new ArrayList<>())
        );
        assertEquals("Start date must be before end date", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenStartDateEqualsEndDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Sprint(START, START, TEAM, "Sprint 1", new ArrayList<>())
        );
        assertEquals("Start date must be before end date", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Constructor - team
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionWhenTeamIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Sprint(START, END, null, "Sprint 1", new ArrayList<>())
        );
        assertEquals("Team can't be null.", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Constructor - description
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionWhenDescriptionIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Sprint(START, END, TEAM, null, new ArrayList<>())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    public void shouldThrowExceptionWhenDescriptionIsBlank(String blank) {
        assertThrows(IllegalArgumentException.class, () ->
                new Sprint(START, END, TEAM, blank, new ArrayList<>())
        );
    }

    // -------------------------------------------------------------------------
    // Constructor - issues
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionWhenIssuesListIsNull() {
        assertThrows(NullPointerException.class, () ->
                new Sprint(START, END, TEAM, "Sprint 1", null)
        );
    }

    // -------------------------------------------------------------------------
    // getIssues - inmutabilidad
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionIfTryingToModifyIssuesListExternally() {
        assertThrows(UnsupportedOperationException.class, () ->
                emptySprint().getIssues().add(issue(8))
        );
    }

    // -------------------------------------------------------------------------
    // setStartDate
    // -------------------------------------------------------------------------

    @Test
    public void shouldAllowUpdatingStartDate() {
        Sprint sprint = emptySprint();
        LocalDate newStart = LocalDate.of(2025, 1, 5);

        sprint.setStartDate(newStart);

        assertEquals(newStart, sprint.getStartDate());
    }

    @Test
    public void shouldThrowExceptionWhenSettingNullStartDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                emptySprint().setStartDate(null)
        );
        assertEquals("Dates cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenSettingStartDateAfterEndDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                emptySprint().setStartDate(END)
        );
        assertEquals("Start date must be before end date", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // setEndDate
    // -------------------------------------------------------------------------

    @Test
    public void shouldAllowUpdatingEndDate() {
        Sprint sprint = emptySprint();
        LocalDate newEnd = LocalDate.of(2025, 2, 28);

        sprint.setEndDate(newEnd);

        assertEquals(newEnd, sprint.getEndDate());
    }

    @Test
    public void shouldThrowExceptionWhenSettingNullEndDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                emptySprint().setEndDate(null)
        );
        assertEquals("Dates cannot be null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenSettingEndDateBeforeStartDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                emptySprint().setEndDate(START)
        );
        assertEquals("Start date must be before end date", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // setTeam
    // -------------------------------------------------------------------------

    @Test
    public void shouldAllowUpdatingTeam() {
        Sprint sprint = emptySprint();
        Team newTeam = new Team("Beta", List.of(new User("jane_doe")));

        sprint.setTeam(newTeam);

        assertEquals(newTeam, sprint.getTeam());
    }

    @Test
    public void shouldThrowExceptionWhenSettingNullTeam() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                emptySprint().setTeam(null)
        );
        assertEquals("Team can't be null.", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // setDescription
    // -------------------------------------------------------------------------

    @Test
    public void shouldAllowUpdatingDescription() {
        Sprint sprint = emptySprint();

        sprint.setDescription("Sprint 2");

        assertEquals("Sprint 2", sprint.getDescription());
    }

    @Test
    public void shouldThrowExceptionWhenSettingNullDescription() {
        assertThrows(NullPointerException.class, () -> emptySprint().setDescription(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    public void shouldThrowExceptionWhenSettingBlankDescription(String blank) {
        assertThrows(IllegalArgumentException.class, () -> emptySprint().setDescription(blank));
    }

    // -------------------------------------------------------------------------
    // calculateSprintDurationInDays
    // -------------------------------------------------------------------------

    @Test
    public void shouldCalculateSprintDurationIncludingBothEndDays() {
        // Jan 1 → Jan 31 = 31 días
        assertEquals(31L, emptySprint().calculateSprintDurationInDays());
    }

    @Test
    public void shouldCalculateSprintDurationOfTwoDays() {
        Sprint sprint = new Sprint(
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 2),
                TEAM, "Sprint 1", new ArrayList<>()
        );
        assertEquals(2L, sprint.calculateSprintDurationInDays());
    }

    // -------------------------------------------------------------------------
    // calculateMaximumWorkableHours
    // -------------------------------------------------------------------------

    @Test
    public void shouldCalculateMaximumWorkableHoursWithOneUser() {
        // 1 usuario * 31 días * 8h = 248h
        assertEquals(248L, emptySprint().calculateMaximumWorkableHours());
    }

    @Test
    public void shouldCalculateMaximumWorkableHoursWithMultipleUsers() {
        Team twoUserTeam = new Team("Beta", List.of(new User("user_one"), new User("user_two")));
        Sprint sprint = new Sprint(START, END, twoUserTeam, "Sprint 1", new ArrayList<>());

        // 2 usuarios * 31 días * 8h = 496h
        assertEquals(496L, sprint.calculateMaximumWorkableHours());
    }

    // -------------------------------------------------------------------------
    // calculateTotalEstimatedHours
    // -------------------------------------------------------------------------

    @Test
    public void calculateTotalEstimatedHoursShouldReturnZeroWhenNoIssues() {
        assertEquals(0, emptySprint().calculateTotalEstimatedHours());
    }

    @Test
    public void shouldCalculateTotalEstimatedHours() {
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1",
                new ArrayList<>(List.of(issue(8), issue(4)))
        );
        assertEquals(12, sprint.calculateTotalEstimatedHours());
    }

    // -------------------------------------------------------------------------
    // addIssue - happy path
    // -------------------------------------------------------------------------

    @Test
    public void shouldAddIssueWithinHoursLimit() {
        Sprint sprint = emptySprint();
        IssueStub issue = issue(8);

        sprint.addIssue(issue);

        assertEquals(1, sprint.getIssues().size());
        assertTrue(sprint.getIssues().contains(issue));
    }

    // -------------------------------------------------------------------------
    // addIssue - validaciones
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionWhenAddingNullIssue() {
        assertThrows(NullPointerException.class, () -> emptySprint().addIssue(null));
    }

    @Test
    public void shouldThrowExceptionWhenAddingDuplicateIssue() {
        IssueStub issue = issue(8);
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1", new ArrayList<>(List.of(issue)));

        assertThrows(IllegalArgumentException.class, () -> sprint.addIssue(issue));
    }

    @Test
    public void shouldThrowExceptionWhenIssueExceedsMaximumWorkableHours() {
        Sprint sprint = emptySprint();
        // 248h disponibles (1 usuario * 31 días * 8h), añadimos una issue que lo supera
        IssueStub issue = issue(249);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                sprint.addIssue(issue)
        );
        assertEquals("Issue's estimated hours exceeds Sprint's limit", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenCumulativeHoursExceedLimit() {
        Sprint sprint = emptySprint();
        sprint.addIssue(issue(240));

        // Quedan 8h libres, intentamos añadir una de 9h
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                sprint.addIssue(issue(9))
        );
        assertEquals("Issue's estimated hours exceeds Sprint's limit", exception.getMessage());
    }

    @Test
    public void shouldAllowAddingIssueExactlyAtHoursLimit() {
        Sprint sprint = emptySprint();
        // 248h disponibles, añadimos exactamente 248h
        sprint.addIssue(issue(248));

        assertEquals(1, sprint.getIssues().size());
    }

    // -------------------------------------------------------------------------
    // removeIssue - happy path
    // -------------------------------------------------------------------------

    @Test
    public void shouldRemoveExistingIssue() {
        IssueStub issue = issue(8);
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1", new ArrayList<>(List.of(issue)));

        sprint.removeIssue(issue);

        assertTrue(sprint.getIssues().isEmpty());
    }

    // -------------------------------------------------------------------------
    // removeIssue - validaciones
    // -------------------------------------------------------------------------

    @Test
    public void shouldThrowExceptionWhenRemovingNullIssue() {
        assertThrows(NullPointerException.class, () -> emptySprint().removeIssue(null));
    }

    @Test
    public void shouldThrowExceptionWhenRemovingIssueNotInSprint() {
        Sprint sprint = emptySprint();
        IssueStub stranger = issue(8);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                sprint.removeIssue(stranger)
        );
        assertEquals("Issue is not linked to this Sprint.", exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // calculateProgressPercentage
    // -------------------------------------------------------------------------

    @Test
    public void calculateProgressPercentageShouldReturnZeroWhenNoIssuesAreDone() {
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1",
                new ArrayList<>(List.of(issue(8), issue(4)))
        );

        assertEquals(0.0, sprint.calculateProgressPercentage());
    }

    @Test
    public void shouldReturnHundredWhenAllIssuesAreDone() {
        IssueStub issue1 = issue(8);
        IssueStub issue2 = issue(4);
        issue1.setProgress(100.0);
        issue2.setProgress(100.0);
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1",
                new ArrayList<>(List.of(issue1, issue2))
        );

        assertEquals(100.0, sprint.calculateProgressPercentage());
    }

    @Test
    public void shouldCalculateProgressPercentageBasedOnEstimatedHours() {
        IssueStub done = issue(8);
        IssueStub pending = issue(8);
        done.setProgress(100.0);
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1",
                new ArrayList<>(List.of(done, pending))
        );

        assertEquals(50.0, sprint.calculateProgressPercentage());
    }

    @Test
    public void shouldWeightProgressByEstimatedHours() {
        // La issue completada pesa más en horas, el % debe reflejarlo
        IssueStub done = issue(6);
        IssueStub pending = issue(2);
        done.setProgress(100.0);
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1",
                new ArrayList<>(List.of(done, pending))
        );

        assertEquals(75.0, sprint.calculateProgressPercentage());
    }

    // -------------------------------------------------------------------------
    // calculateTotalEstimatedCost
    // -------------------------------------------------------------------------

    @Test
    public void calculateTotalEstimatedCostShouldReturnZeroWhenNoIssues() {
        assertEquals(BigDecimal.ZERO, emptySprint().calculateTotalEstimatedCost());
    }

    @Test
    public void shouldCalculateTotalEstimatedCost() {
        // COST_PER_HOUR = 10, issue(8) → 80, issue(4) → 40, total → 120
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1",
                new ArrayList<>(List.of(issue(8), issue(4)))
        );

        assertEquals(BigDecimal.valueOf(120), sprint.calculateTotalEstimatedCost());
    }

    @Test
    public void shouldCalculateTotalEstimatedCostWithSingleIssue() {
        Sprint sprint = new Sprint(START, END, TEAM, "Sprint 1",
                new ArrayList<>(List.of(issue(5)))
        );

        assertEquals(BigDecimal.valueOf(50), sprint.calculateTotalEstimatedCost());
    }
}
