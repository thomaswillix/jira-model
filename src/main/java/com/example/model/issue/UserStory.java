package com.example.model.issue;

import lombok.Getter;

import java.math.BigDecimal;

import static com.example.functions.Validation.requireNonNull;

@Getter
public class UserStory extends Issue {

    private Integer complexityPoints;

    public UserStory(
            String title, String description, Integer estimatedHours, Double progress, Integer complexityPoints
    ) {
        super(title, description, estimatedHours, progress);
        this.complexityPoints = requireValidComplexityPoints(complexityPoints);
    }

    public void setComplexityPoints(Integer complexityPoints) {
        this.complexityPoints = requireValidComplexityPoints(complexityPoints);
    }

    private Integer requireValidComplexityPoints(Integer complexityPoints){
        requireNonNull(complexityPoints, "Complexity points");
        if (complexityPoints < 0) throw new IllegalArgumentException("Complexity points can't be negative");
        return complexityPoints;
    }

    @Override
    public BigDecimal calculateEstimatedCost() {
        return super.calculateEstimatedCost();
    }
}
