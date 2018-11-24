package com.crossover.techtrial.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    private DateUtils() {
    }

    /**
     * Get a current time
     *
     * @return an instance of Date representing the current date time on the
     * system timezone
     */
    public static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static Instant parse(String value) {
        return Instant.parse(value);
    }

    public static Instant addHours(Instant instant, int hours) {
        return instant.plus(hours, ChronoUnit.HOURS);
    }

    public static Instant addMinutes(Instant instant, int minutes) {
        return instant.plus(minutes, ChronoUnit.MINUTES);
    }

}
