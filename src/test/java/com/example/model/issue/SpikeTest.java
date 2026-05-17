package com.example.model.issue;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpikeTest {

    private Spike spike;

    @Test
    public void shouldCreateValidSpike() {
        spike = new Spike(
                "Research OAuth2 Integration",
                "Proof of concept with architecture diagram",
                8,
                0.0
        );

        assertEquals("Research OAuth2 Integration", spike.getTitle());
        assertEquals("Proof of concept with architecture diagram", spike.getDescription());
        assertEquals(8, spike.getEstimatedHours());
        assertEquals(0.0, spike.getProgress());
        assertEquals(IssueStatus.PENDING, spike.getStatus());
    }

    // calculateEstimatedCost()

    @Test
    public void shouldCalculateEstimatedCostIncludingSoftwareInstallationCost() {
        // (4 hours * 10/hour base cost) + 50 installation cost = 90
        spike = new Spike(
                "Evaluate New IDE Setup",
                "Performance comparison report",
                4,
                0.0
        );
        BigDecimal expectedCost = BigDecimal.valueOf(90);

        // Using compareTo for BigDecimal to bypass strict scale mismatches (.0 vs .00)
        assertEquals(0, expectedCost.compareTo(spike.calculateEstimatedCost()));
    }

    @Test
    public void shouldRecalculateCostWhenBaseEstimatedHoursMutate() {
        // Initial: (2 hours * 10/hour base cost) + 50 = 70
        spike = new Spike(
                "Spike Title",
                "Expected Result",
                2,
                0.0
        );
        assertEquals(0, BigDecimal.valueOf(70).compareTo(spike.calculateEstimatedCost()));

        spike.setEstimatedHours(10);
        BigDecimal expectedNewCost = BigDecimal.valueOf(150); // (10 * 10) + 50 = 150

        assertEquals(0, expectedNewCost.compareTo(spike.calculateEstimatedCost()));
    }
}
