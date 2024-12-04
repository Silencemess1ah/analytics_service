package faang.school.analytics.model;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;

@Getter
public enum Interval {
    HOUR(Duration.ofHours(1)),
    DAY(Duration.ofDays(1)),
    WEEK(Period.ofWeeks(1)),
    MONTH(Period.ofMonths(1));

    private final TemporalAmount interval;

    Interval(TemporalAmount interval) {
        this.interval = interval;
    }

    public LocalDateTime calculateIntervalStart() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        return now.minus(interval);
    }
}

