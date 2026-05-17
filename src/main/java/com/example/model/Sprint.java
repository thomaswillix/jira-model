package com.example.model;

import com.example.model.issue.Issue;
import com.example.model.issue.IssueStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.functions.Validation.*;

@Getter
public class Sprint{
    private LocalDate startDate;
    private LocalDate endDate;
    private Team team;
    private String description;
    private final List<Issue> issues;

    private static final Integer WORKING_HOURS_PER_DAY = 8;

    public Sprint (LocalDate startDate, LocalDate endDate, Team team, String description, List<Issue> issues){
        requireValidDates(startDate, endDate);
        requireNonNull(team, "Team");
        requireNonNull(issues, "Issue list");

        this.startDate = startDate;
        this.endDate = endDate;
        this.team = team;
        this.description = requireValidString(description, "Sprint description");
        this.issues = new ArrayList<>(issues);
    }

    public List<Issue> getIssues() {
        return Collections.unmodifiableList(this.issues);
    }

    public void setStartDate(LocalDate newStartDate) {
        requireValidDates(newStartDate, this.endDate);

        this.startDate = newStartDate;
    }

    public void setEndDate(LocalDate newEndDate) {
        requireValidDates(this.startDate, newEndDate);

        this.endDate = newEndDate;
    }

    public void setTeam(Team team) {
        requireNonNull(team, "Team");
        this.team = team;
    }

    public void setDescription(String description) {
        this.description = requireValidString(description, "Sprint description");
    }

    public void addIssue(Issue newIssue) {
        requireNonNull(newIssue, "Issue");
        if (this.issues.contains(newIssue))
            throw new IllegalArgumentException("Issue is already linked to this Sprint.");

        if (calculateTotalEstimatedHours() + newIssue.getEstimatedHours() > calculateMaximumWorkableHours())
            throw new IllegalArgumentException("Issue's estimated hours exceeds Sprint's limit");
        this.issues.add(newIssue);
    }

    public void removeIssue(Issue issue) {
        requireNonNull(issue, "Issue");

        if (!this.issues.remove(issue))
            throw new IllegalArgumentException("Issue is not linked to this Sprint.");
    }

    private void requireValidDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (!startDate.isBefore(endDate))
            throw new IllegalArgumentException("Start date must be before end date");
    }

    public Integer calculateTotalEstimatedHours() {
        return issues.stream()
                .mapToInt(Issue::getEstimatedHours)
                .sum();
    }

    public Long calculateSprintDurationInDays(){
        // + 1 to include the last day
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public Long calculateMaximumWorkableHours(){
        if (team.getUsers().isEmpty()) {
            return 0L;
        }
        return WORKING_HOURS_PER_DAY * team.getUsers().size() * calculateSprintDurationInDays();
    }

    public Double calculateProgressPercentage(){
        if (issues.isEmpty()) return 0.0;
        int completedHours = issues.stream()
                .filter(issue -> issue.getStatus() == IssueStatus.DONE)
                .mapToInt(Issue::getEstimatedHours)
                .sum();

        return (double) completedHours / calculateTotalEstimatedHours() * 100;
    }

    public BigDecimal calculateTotalEstimatedCost() {
        if (issues.isEmpty()) return BigDecimal.ZERO;
        return issues.stream()
                .map(Issue::calculateEstimatedCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
