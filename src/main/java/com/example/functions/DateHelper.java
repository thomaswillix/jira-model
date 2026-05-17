package com.example.functions;

import java.time.LocalDate;

public class DateHelper {

    public static boolean isBetween(LocalDate date, LocalDate from, LocalDate to) {
        return date.isAfter(from) && date.isBefore(to);
    }

    public static boolean isContainedInRange(
            LocalDate begin, LocalDate end, LocalDate sprintBegin, LocalDate sprintEnd
    ) {
        return begin.isBefore(sprintBegin) && end.isAfter(sprintEnd);
    }
}
