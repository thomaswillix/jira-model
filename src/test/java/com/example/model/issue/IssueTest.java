package com.example.model.issue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IssueTest {

    private IssueStub issue;

    @Test
    public void shouldNotAllowNullTitleInConstructor() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new IssueStub(null, "Description", 10, 0.0)
        );
        assertEquals("Title can't be null.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowBlankTitleInConstructor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new IssueStub("   ", "Description", 10, 0.0)
        );
        assertEquals("Title can't be blank.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowNullDescriptionInConstructor() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new IssueStub("Title", null, 10, 0.0)
        );
        assertEquals("Description can't be null.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowBlankDescriptionInConstructor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new IssueStub("Title", " ", 10, 0.0)
        );
        assertEquals("Description can't be blank.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowNullEstimatedHoursInConstructor() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new IssueStub("Valid Title", "Description", null, 0.0)
        );
        assertEquals("Estimated hours can't be null.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowNegativeEstimatedHoursInConstructor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new IssueStub("Valid Title", "Description", -5, 0.0)
        );
        assertEquals("Estimated hours can't be negative", exception.getMessage());
    }

    @Test
    public void shouldNotAllowNullProgressInConstructor() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new IssueStub("Valid Title", "Description", 10, null)
        );
        assertEquals("Progress can't be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -50.0, 100.01, 200.0})
    public void shouldNotAllowInvalidProgressRangesInConstructor(Double invalidProgress) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new IssueStub("Valid Title", "Description", 10, invalidProgress)
        );
        assertEquals("El progreso debe ser un porcentaje entre 0 y 100", exception.getMessage());
    }

    // Status transition validations

    @ParameterizedTest
    @MethodSource("provideProgressAndStatusPairs")
    public void shouldAssignCorrectStatusBasedOnProgressInConstructor(Double progress, IssueStatus expectedStatus) {
        issue = new IssueStub("Title", "Desc", 10, progress);

        assertEquals(expectedStatus, issue.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideProgressAndStatusPairs")
    public void shouldUpdateStatusCorrectlyWhenProgressChangesViaSetter(Double progress, IssueStatus expectedStatus) {
        // Always initialize in PENDING state with 0.0 progress
        issue = new IssueStub("Title", "Desc", 10, 0.0);

        issue.setProgress(progress);

        assertEquals(progress, issue.getProgress());
        assertEquals(expectedStatus, issue.getStatus());
    }

    // setters

    @Test
    public void shouldUpdateTitleCorrectlyWhenValid() {
        issue = new IssueStub("Old Title", "Desc", 10, 0.0);
        issue.setTitle("New Valid Title");
        assertEquals("New Valid Title", issue.getTitle());
    }

    @Test
    public void shouldNotAllowBlankTitleViaSetter() {
        issue = new IssueStub("Old Title", "Desc", 10, 0.0);
        assertThrows(IllegalArgumentException.class, () -> issue.setTitle(""));
    }

    @Test
    public void shouldNotAllowNullTitleViaSetter() {
        issue = new IssueStub("Old Title", "Desc", 10, 0.0);
        assertThrows(NullPointerException.class, () -> issue.setTitle(null));
    }

    @Test
    public void shouldNotAllowBlankDescriptionViaSetter() {
        issue = new IssueStub("Old Title", "Desc", 10, 0.0);
        assertThrows(IllegalArgumentException.class, () -> issue.setDescription(""));
    }

    @Test
    public void shouldNotAllowNullDescriptionViaSetter() {
        issue = new IssueStub("Old Title", "Desc", 10, 0.0);
        assertThrows(NullPointerException.class, () -> issue.setDescription(null));
    }

    @Test
    public void shouldUpdateEstimatedHoursCorrectlyWhenValid() {
        issue = new IssueStub("Title", "Desc", 10, 0.0);
        issue.setEstimatedHours(25);
        assertEquals(25, issue.getEstimatedHours());
    }

    @Test
    public void shouldNotAllowNegativeHoursViaSetter() {
        issue = new IssueStub("Title", "Desc", 10, 0.0);
        assertThrows(IllegalArgumentException.class, () -> issue.setEstimatedHours(-1));
    }

    @Test
    public void shouldNotAllowInvalidProgressViaSetter() {
        issue = new IssueStub("Title", "Desc", 10, 0.0);
        assertThrows(IllegalArgumentException.class, () -> issue.setProgress(150.0));
    }

    // calculateEstimatedCost()

    @Test
    public void shouldCalculateBaseEstimatedCostCorrectly() {
        // 12 hours * 10 euros/hour = 120 euros
        issue = new IssueStub("Title", "Desc", 12, 0.0);

        BigDecimal expectedCost = BigDecimal.valueOf(120);
        assertEquals(0, expectedCost.compareTo(issue.calculateEstimatedCost()));
    }


    // Private helpers

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> provideProgressAndStatusPairs() {
        return java.util.stream.Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(0.0, IssueStatus.PENDING),
                org.junit.jupiter.params.provider.Arguments.of(0.1, IssueStatus.IN_PROGRESS),
                org.junit.jupiter.params.provider.Arguments.of(50.0, IssueStatus.IN_PROGRESS),
                org.junit.jupiter.params.provider.Arguments.of(99.9, IssueStatus.IN_PROGRESS),
                org.junit.jupiter.params.provider.Arguments.of(100.0, IssueStatus.DONE)
        );
    }
}
