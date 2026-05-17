package com.example.model.issue;

import com.example.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BugTest {

    private static final User dummyUser = new User("john_doe");
    private Bug bug;

    @Test
    public void shouldCreateValidBug() {
        bug = new Bug(
                "[API] User retrieved has null ID number",
                "User retieved via API has a null ID number", Environment.INTEGRATION,
                BusinessImpact.MEDIUM, dummyUser, 3, 20.0, BigDecimal.valueOf(100)
        );
        assertEquals("[API] User retrieved has null ID number", bug.getTitle());
        assertEquals("User retieved via API has a null ID number", bug.getDescription());
        assertEquals(Environment.INTEGRATION, bug.getEnvironment());
        assertEquals(BusinessImpact.MEDIUM, bug.getBusinessImpact());
        assertEquals(dummyUser.getUserName(), bug.getReporter());
        assertEquals(3, bug.getEstimatedHours());
        assertEquals(20.0, bug.getProgress());
        assertEquals(BigDecimal.valueOf(100), bug.getMonetaryImpact());
    }

    @ParameterizedTest
    @EnumSource(Environment.class)
    public void shouldCreateBugWithAnyEnvironment(Environment environment) {
        bug = new Bug(
                "Home page does not load", "Error description", environment, BusinessImpact.MEDIUM,
                dummyUser, 1, 10.0, BigDecimal.valueOf(1000)
        );
        assertNotNull(bug);
        assertEquals(environment, bug.getEnvironment());
    }

    @ParameterizedTest
    @EnumSource(BusinessImpact.class)
    void shouldCreateBugWithAnyBusinessImpact(BusinessImpact impact) {
        bug = new Bug(
                "Fix login crash", "App crashes on login", Environment.PRODUCTION, impact,
                dummyUser, 5, 0.0, BigDecimal.TEN
        );

        assertNotNull(bug);
        assertEquals(impact, bug.getBusinessImpact());
    }

    @Test
    public void shouldNotAllowToCreateBugWithNegativeMonetaryImpact() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new Bug(
                        "API returns 500 with GET /api/v1/users","Error description", Environment.INTEGRATION,
                        BusinessImpact.MEDIUM, dummyUser, 1, 0.0, BigDecimal.valueOf(-100)
                )
        );
        assertEquals("Monetary impact can't be negative.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowToCreateBugWithNullMonetaryImpact() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Bug(
                        "API returns 500 with GET /api/v1/products", "Error description", Environment.INTEGRATION,
                        BusinessImpact.MEDIUM, dummyUser, 1, 10.0, null
                )
        );
        assertEquals("Monetary impact can't be null.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowToCreateBugWithNullEnvironment() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Bug(
                        "API returns 500 with GET /api/v1/products", "Error description",
                        null, BusinessImpact.MEDIUM, dummyUser, 1, 10.0, BigDecimal.TEN
                )
        );
        assertEquals("Environment can't be null.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowToCreateBugWithNullBusinessImpact() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Bug(
                        "API returns 500 with GET /api/v1/products", "Error description", Environment.INTEGRATION,
                        null, dummyUser, 1, 10.0, BigDecimal.TEN
                )
        );
        assertEquals("Business impact can't be null.", exception.getMessage());
    }

    // setters

    @Test
    public void shouldAllowToChangeMonetaryImpactValue() {
        bug = new Bug(
                "API returns 500 with POST /api/v1/users", "Error description", Environment.INTEGRATION,
                BusinessImpact.MEDIUM, dummyUser, 1, 0.0, BigDecimal.valueOf(100)
        );
        bug.setMonetaryImpact(BigDecimal.valueOf(10000));
        assertEquals(BigDecimal.valueOf(10000), bug.getMonetaryImpact());
    }

    @Test
    public void shouldAllowToChangeReporterUsingUserObject() {
        bug = new Bug(
                "Bug title", "Error description", Environment.INTEGRATION, BusinessImpact.HIGH,
                dummyUser, 1, 0.0, BigDecimal.TEN
        );
        User newReporter = new User("jane_smith");

        bug.setReporter(newReporter);

        assertEquals("jane_smith", bug.getReporter());
    }

    @Test
    public void shouldNotAllowToSetNegativeMonetaryImpactViaSetter() {
        bug = new Bug(
                "Bug title", "Negative monetary impact shouldn't be allowed", Environment.INTEGRATION,
                BusinessImpact.HIGH, dummyUser, 1, 50.0, BigDecimal.valueOf(100)
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bug.setMonetaryImpact(BigDecimal.valueOf(-50))
        );
        assertEquals("Monetary impact can't be negative.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowToSetNullMonetaryImpactViaSetter() {
        bug = new Bug(
                "Bug title", "Null monetary impact shouldn't be allowed", Environment.PRODUCTION,
                BusinessImpact.CRITICAL, dummyUser, 2, 10.0, BigDecimal.TEN
        );

        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                bug.setMonetaryImpact(null)
        );
        assertEquals("Monetary impact can't be null.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowToSetNullEnvironmentViaSetter() {
        bug = new Bug(
                "Bug title", "Null environment shouldn't be allowed", Environment.PRODUCTION,
                BusinessImpact.CRITICAL, dummyUser, 2, 10.0, BigDecimal.TEN
        );

        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                bug.setEnvironment(null)
        );
        assertEquals("Environment can't be null.", exception.getMessage());
    }

    @Test
    public void shouldNotAllowToSetNullBusinessImpactViaSetter() {
        bug = new Bug(
                "Bug title", "Null business impact shouldn't be allowed", Environment.PRODUCTION,
                BusinessImpact.CRITICAL, dummyUser, 2, 10.0, BigDecimal.TEN
        );

        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                bug.setBusinessImpact(null)
        );
        assertEquals("Business impact can't be null.", exception.getMessage());
    }

    // calculateEstimatedCost()

    @Test
    public void shouldCalculateEstimatedCostCorrectlyWithZeroHours() {
        // (0 estimatedHours * 10) + 500 monetaryImpact = 500
        bug = new Bug(
                "Bug title", "Error description", Environment.PREPRODUCTION, BusinessImpact.CRITICAL,
                dummyUser, 0, 0.0, BigDecimal.valueOf(500)
        );

        BigDecimal expectedCost = BigDecimal.valueOf(500);
        assertEquals(0, expectedCost.compareTo(bug.calculateEstimatedCost()));
    }

    @Test
    public void shouldCalculateEstimatedCostCorrectlyWithHoursAndImpact() {
        // (5 estimatedHours * 10) + 250 monetaryImpact = 300
        bug = new Bug(
                "Bug title", "Error description", Environment.PRODUCTION, BusinessImpact.CRITICAL,
                dummyUser, 5, 0.0, BigDecimal.valueOf(250)
        );

        BigDecimal expectedCost = BigDecimal.valueOf(300);
        // Using compareTo for BigDecimal to bypass strict scale mismatches (.0 vs .00)
        assertEquals(0, expectedCost.compareTo(bug.calculateEstimatedCost()));
    }

    @Test
    public void shouldRecalculateEstimatedCostWhenMonetaryImpactChanges() {
        // Initial: (2 estimatedHours * 10) + 100 monetaryImpact = 120
        bug = new Bug(
                "Bug title", "Error description", Environment.INTEGRATION, BusinessImpact.HIGH,
                dummyUser, 2, 0.0, BigDecimal.valueOf(100)
        );
        assertEquals(0, BigDecimal.valueOf(120).compareTo(bug.calculateEstimatedCost()));

        // Changing monetary impact to 500 -> New cost: 20 + 500 = 520
        bug.setMonetaryImpact(BigDecimal.valueOf(500));

        assertEquals(0, BigDecimal.valueOf(520).compareTo(bug.calculateEstimatedCost()));
    }

    @Test
    public void shouldRecalculateEstimatedCostWhenEstimatedHoursChange() {
        // Initial: (2 estimatedHours * 10) + 100 monetaryImpact = 120
        bug = new Bug(
                "Bug title", "Error description", Environment.INTEGRATION, BusinessImpact.CRITICAL,
                dummyUser, 2, 0.0, BigDecimal.valueOf(100)
        );

        // Changing estimated hours from base class to 10 -> New cost: 100 + 100 = 200
        bug.setEstimatedHours(10);

        assertEquals(0, BigDecimal.valueOf(200).compareTo(bug.calculateEstimatedCost()));
    }
}
