package com.example.model.issue;

import java.math.BigDecimal;

public class Spike extends Issue {

    private static final BigDecimal SOFTWARE_INSTALLATION_COST = BigDecimal.valueOf(50);

    public Spike(String title, String expectedResult, Integer estimatedHours, Double progress) {
        super(title, expectedResult, estimatedHours, progress);
    }

    @Override
    public BigDecimal calculateEstimatedCost() {
        return super.calculateEstimatedCost().add(SOFTWARE_INSTALLATION_COST);
    }

    @Override
    public String toString() {
        return "Spike{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", estimatedHours=" + getEstimatedHours() +
                ", progress=" + getProgress() +
                ", status=" + getStatus().name() +
                '}';
    }
}
