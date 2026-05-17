package com.example.model.issue;

import java.math.BigDecimal;

// STUB: Minimal concrete subclass to instantiate and isolate Issue tests
public class IssueStub extends Issue {
    public IssueStub(String title, String description, Integer estimatedHours, Double progress) {
        super(title, description, estimatedHours, progress);
    }

    // Expose the protected method as public to test its base calculation
    @Override
    public BigDecimal calculateEstimatedCost() {
        return super.calculateEstimatedCost();
    }
}