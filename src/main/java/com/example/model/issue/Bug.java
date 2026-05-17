package com.example.model.issue;


import com.example.model.User;
import lombok.Getter;

import java.math.BigDecimal;

import static com.example.functions.Validation.requireNonNull;

@Getter
public class Bug extends Issue {

    private Environment environment;
    private BusinessImpact businessImpact;
    private BigDecimal monetaryImpact;
    private String reporter;

    public Bug(
            String title, String errorDescription, Environment environment, BusinessImpact businessImpact, User reporter,
            Integer hoursEstimation, Double progress, BigDecimal monetaryImpact
    ) {
        super(title,errorDescription, hoursEstimation, progress);
        requireNonNull(businessImpact, "Business impact");
        requireNonNull(environment, "Environment");
        requireNonNull(reporter, "User");
        this.environment = environment;
        this.businessImpact = businessImpact;
        this.reporter = reporter.getUserName();
        this.monetaryImpact = requireValidMonetaryImpact(monetaryImpact);
    }

    public void setMonetaryImpact(BigDecimal monetaryImpact) {
        this.monetaryImpact = requireValidMonetaryImpact(monetaryImpact);
    }

    public void setEnvironment(Environment environment) {
        requireNonNull(environment, "Environment");
        this.environment = environment;
    }

    public void setBusinessImpact(BusinessImpact businessImpact) {
        requireNonNull(businessImpact, "Business impact");
        this.businessImpact = businessImpact;
    }

    public void setReporter(User reporter) {
        requireNonNull(reporter, "User");
        this.reporter = reporter.getUserName();
    }

    private BigDecimal requireValidMonetaryImpact(BigDecimal monetaryImpact){
        requireNonNull(monetaryImpact, "Monetary impact");
        if (monetaryImpact.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Monetary impact can't be negative.");

        return monetaryImpact;
    }

    @Override
    public BigDecimal calculateEstimatedCost() {
        return super.calculateEstimatedCost().add(monetaryImpact);
    }
}
