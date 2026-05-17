package com.example.model.issue;

import lombok.Getter;

import java.math.BigDecimal;

import static com.example.functions.Validation.*;

@Getter
public abstract class Issue {

    private String title;
    private String description;
    private Integer estimatedHours;
    private Double progress;
    private IssueStatus status;

    protected final static BigDecimal COST_PER_HOUR = BigDecimal.valueOf(10);

    public Issue(
            String title, String description,
            Integer estimatedHours, Double progress
    ) {
        this.title = requireValidString(title, "Title");
        this.description = requireValidString(description, "Description");
        this.estimatedHours = requireValidEstimatedHours(estimatedHours);
        this.progress = requireValidProgress(progress);
        this.status = updateStatus();
    }

    public BigDecimal calculateEstimatedCost(){
        return BigDecimal.valueOf(getEstimatedHours()).multiply(COST_PER_HOUR);
    }

    public void setTitle(String title) {
        this.title = requireValidString(title, "Title");
    }

    public void setDescription(String description) {
        this.description = requireValidString(description, "Description");
    }

    public void setEstimatedHours(Integer estimatedHours) {
        this.estimatedHours = requireValidEstimatedHours(estimatedHours);
    }

    public void setProgress(Double progress) {
        this.progress = requireValidProgress(progress);
        this.status = updateStatus();
    }

    private Integer requireValidEstimatedHours(Integer estimatedHours) {
        requireNonNull(estimatedHours, "Estimated hours");
        if (estimatedHours < 0) throw new IllegalArgumentException("Estimated hours can't be negative");
        return estimatedHours;
    }

    private Double requireValidProgress(Double progress) {
        requireNonNull(progress, "Progress");
        if (progress < 0.0 || progress > 100.0)
            throw new IllegalArgumentException("El progreso debe ser un porcentaje entre 0 y 100");

        return progress;
    }

    private IssueStatus updateStatus() {
        if (this.progress == 0.0) return IssueStatus.PENDING;
        if (this.progress == 100.0) return IssueStatus.DONE;
        return IssueStatus.IN_PROGRESS;
    }
}
