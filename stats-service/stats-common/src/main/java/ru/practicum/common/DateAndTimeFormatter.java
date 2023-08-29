package ru.practicum.common;

import java.time.format.DateTimeFormatter;

public class DateAndTimeFormatter {

    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}