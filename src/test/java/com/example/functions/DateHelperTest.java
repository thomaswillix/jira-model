package com.example.functions;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateHelperTest {

    @Test
    public void givenThreeDatesWhenIsBetweenThenReturnsTrue() {
        assertTrue(
                DateHelper.isBetween(LocalDate.of(2023,4,2),
                        LocalDate.of(2023,4,1),
                        LocalDate.of(2023,4,5)));

    }

    @Test
    public void givenThreeDatesWhenIsBetweenThenReturnsFalse() {
        assertFalse(
                DateHelper.isBetween(LocalDate.of(2023,4,12),
                        LocalDate.of(2023,4,1),
                        LocalDate.of(2023,4,5)));

    }

    @Test
    public void givenTwoDateRangesWhenSecondDateIsContainedInFirstRangeThenReturnsTrue() {
        assertFalse(
                DateHelper.isContainedInRange(LocalDate.of(2023,4,5),
                        LocalDate.of(2023,4,10),
                        LocalDate.of(2023,4,1),
                        LocalDate.of(2023,4,12)
                        ));

    }

    @Test
    public void givenTwoDateRangesWhenSecondDateIsNotContainedInFirstRangeThenReturnsFalse() {
        assertFalse(
                DateHelper.isContainedInRange(LocalDate.of(2023,4,1),
                        LocalDate.of(2023,4,7),
                        LocalDate.of(2023,4,5),
                        LocalDate.of(2023,4,12)
                ));

    }

}