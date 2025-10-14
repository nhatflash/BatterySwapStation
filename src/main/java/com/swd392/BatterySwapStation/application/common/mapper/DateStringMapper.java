package com.swd392.BatterySwapStation.application.common.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateStringMapper {

    private static final List<String> datePatterns = List.of(
            "yyyy-MM-dd",
            "dd/MM/yyyy",
            "MM/dd/yyyy",
            "dd-MM-yyyy",
            "yyyy/MM/dd",
            "MM-dd-yyyy"
    );

    private static final List<String> dateTimePatterns = List.of(
            "yyyy-MM-dd HH:mm:ss",
            "dd/MM/yyyy HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss",
            "dd-MM-yyyy HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "MM-dd-yyyy HH:mm:ss"
    );

    private static final List<String> timePatterns = List.of(
            "HH:mm:ss",
            "HH.mm.ss",
            "HHmmss"
    );

    public static LocalDate getLocalDate(String dateString) {
        for (String pattern : datePatterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {

            }
        }
        throw new IllegalArgumentException("Invalid request date string: " + dateString);
    }

    public static LocalDateTime getLocalDateTime(String dateString) {
        for (String pattern : dateTimePatterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {

            }
        }
        throw new IllegalArgumentException("Invalid request date time string: " + dateString);
    }

    public static LocalTime getLocalTime(String timeString) {
        for (String pattern : timePatterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalTime.parse(timeString, formatter);
            } catch (DateTimeParseException ignored) {}
        }
        throw new IllegalArgumentException("Invalid request time string: " + timeString);
    }
}
