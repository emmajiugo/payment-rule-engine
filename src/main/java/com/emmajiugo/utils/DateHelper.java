package com.emmajiugo.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

public class DateHelper {

    private static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");
    private static final DateTimeFormatter DATE_TIME_PATTERN = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
                    .toFormatter();

    public static Long getCurrentTimeInMilliseconds() {
        return Instant.now().toEpochMilli();
    }

    public static LocalDateTime getCurrentTimeInUtc() {
        return LocalDateTime.ofInstant(Instant.now(), ZONE_ID_UTC);
    }

    public static LocalDateTime parseDateTime(String dateTimeString) {
        try {
            if (dateTimeString == null) {
                return null;
            }
            return LocalDateTime.parse(dateTimeString, DATE_TIME_PATTERN);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Illegal date/time format", e);
        }
    }

    public static Date parseDate(String dateString) {
        try {
            final Instant instant = Objects.requireNonNull(parseDateTime(dateString))
                            .atZone(ZONE_ID_UTC)
                            .toInstant();
            return Date.from(instant);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Illegal date/time format", e);
        }
    }

    public static Long convertDateToMilliseconds(Date date) {
        return date.getTime();
    }

    public static Long parseDateToMilliseconds(String dateString) {
        return convertDateToMilliseconds(parseDate(dateString));
    }

    private static Long diffIn(LocalDateTime dateTime, ChronoUnit unit) {
        LocalDateTime now = LocalDateTime.now();

        return switch (unit) {
            case DAYS -> ChronoUnit.DAYS.between(dateTime, now);
            case WEEKS -> ChronoUnit.WEEKS.between(dateTime, now);
            case MONTHS -> ChronoUnit.MONTHS.between(dateTime, now);
            case YEARS -> ChronoUnit.YEARS.between(dateTime, now);
            default -> throw new RuntimeException("Unsupported unit. Use DAYS, MONTHS, or YEARS.");
        };
    }

    public static Long diffInDays(LocalDateTime dateTime) {
        return diffIn(dateTime, ChronoUnit.DAYS);
    }

    public static Long diffInMonths(LocalDateTime dateTime) {
        return diffIn(dateTime, ChronoUnit.MONTHS);
    }

    public static Long diffInYears(LocalDateTime dateTime) {
        return diffIn(dateTime, ChronoUnit.YEARS);
    }
}
