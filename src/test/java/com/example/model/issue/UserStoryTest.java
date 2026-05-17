package com.example.model.issue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserStoryTest {

    private UserStory userStory;

    @Test
    public void shouldCreateValidUserStory() {
        userStory = new UserStory(
                "As a user I want to reset my password",
                "Password reset via email token link",
                12,
                0.0,
                5
        );

        assertEquals("As a user I want to reset my password", userStory.getTitle());
        assertEquals("Password reset via email token link", userStory.getDescription());
        assertEquals(12, userStory.getEstimatedHours());
        assertEquals(0.0, userStory.getProgress());
        assertEquals(5, userStory.getComplexityPoints());
        assertEquals(IssueStatus.PENDING, userStory.getStatus());
    }

    @Test
    public void shouldNotAllowNullComplexityPointsInConstructor() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new UserStory("Story Title", "Desc", 8, 0.0, null)
        );
        assertEquals("Complexity points can't be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    public void shouldNotAllowNegativeComplexityPointsInConstructor(Integer negativePoints) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new UserStory("Story Title", "Desc", 8, 0.0, negativePoints)
        );
        assertEquals("Complexity points can't be negative", exception.getMessage());
    }

    // setters

    @Test
    public void shouldAllowToChangeComplexityPointsWhenValid() {
        userStory = new UserStory("Story Title", "Desc", 8, 0.0, 3);

        userStory.setComplexityPoints(8);

        assertEquals(8, userStory.getComplexityPoints());
    }

    @Test
    public void shouldNotAllowToSetNullComplexityPointsViaSetter() {
        userStory = new UserStory("Story Title", "Desc", 8, 0.0, 3);

        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                userStory.setComplexityPoints(null)
        );
        assertEquals("Complexity points can't be null.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowToSetNegativeComplexityPointsViaSetter() {
        userStory = new UserStory("Story Title", "Desc", 8, 0.0, 3);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userStory.setComplexityPoints(-2)
        );
        assertEquals("Complexity points can't be negative", exception.getMessage());
    }

    // calculateEstimatedCost()

    @Test
    public void shouldCalculateEstimatedCostUsingOnlyBaseHours() {
        // 6 estimated hours * 10/hour base cost = 60
        userStory = new UserStory("Story Title", "Desc", 6, 0.0, 13);
        BigDecimal expectedCost = BigDecimal.valueOf(60);

        assertEquals(0, expectedCost.compareTo(userStory.calculateEstimatedCost()));
    }
}
