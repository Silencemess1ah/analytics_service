package faang.school.analytics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Interval {
    INTERVAL_1D(1),
    INTERVAL_3D(3),
    INTERVAL_7D(7),
    INTERVAL_14D(14),
    INTERVAL_30D(30),
    INTERVAL_90D(90),
    INTERVAL_180D(180),
    INTERVAL_365D(365)
    ;

    @Getter
    private final int days;

    public static Interval of(int intervalOrder) {
        for (Interval interval : Interval.values()) {
            if (interval.ordinal() == intervalOrder) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown interval: " + intervalOrder);
    }
}
