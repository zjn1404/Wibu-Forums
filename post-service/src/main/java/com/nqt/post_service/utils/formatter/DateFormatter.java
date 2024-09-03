package com.nqt.post_service.utils.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class DateFormatter {
    Map<Long, Function<Date, String>> formatterMap;

    public DateFormatter() {
        formatterMap = new LinkedHashMap<>();
        formatterMap.put(60L, this::formatSeconds);
        formatterMap.put(3600L, this::formatMinutes);
        formatterMap.put(86400L, this::formatHours);
        formatterMap.put(Long.MAX_VALUE, this::formatDays);
    }

    public String format(Date date) {
        long elapsedSeconds = ChronoUnit.SECONDS.between(date.toInstant(), Instant.now());

        Function<Date, String> formatter = formatterMap.entrySet().stream()
                .filter(longFunctionEntry -> elapsedSeconds < longFunctionEntry.getKey())
                .findFirst()
                .get()
                .getValue();

        return formatter.apply(date);
    }

    private String formatSeconds(Date date) {
        long elapsedSeconds = ChronoUnit.SECONDS.between(date.toInstant(), Instant.now());

        return String.format("%d seconds", elapsedSeconds);
    }

    private String formatMinutes(Date date) {
        long elapsedMinutes = ChronoUnit.MINUTES.between(date.toInstant(), Instant.now());

        return String.format("%d minutes", elapsedMinutes);
    }

    private String formatHours(Date date) {
        long elapsedHours = ChronoUnit.HOURS.between(date.toInstant(), Instant.now());

        return String.format("%d hours", elapsedHours);
    }

    private String formatDays(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        return localDateTime.format(formatter);
    }
}
