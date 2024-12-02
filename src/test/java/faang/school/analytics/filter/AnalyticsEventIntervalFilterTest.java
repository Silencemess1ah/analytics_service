package faang.school.analytics.filter;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsEventIntervalFilterTest {
    private AnalyticsEventIntervalFilter analyticsEventIntervalFilter;
    private AnalyticsEventFilterDto filter;
    private Stream<AnalyticsEvent> events;

    @BeforeEach
    void setUp() {
        analyticsEventIntervalFilter = new AnalyticsEventIntervalFilter();
        filter = AnalyticsEventFilterDto.builder()
                .interval(Interval.WEEK)
                .build();

        events = Stream.of(
                AnalyticsEvent.builder()
                        .receivedAt(LocalDateTime.now(ZoneId.of("UTC")).minus(Duration.ofDays(2)))
                        .build(),
                AnalyticsEvent.builder()
                        .receivedAt(LocalDateTime.now(ZoneId.of("UTC")).minus(Duration.ofDays(10)))
                        .build()
        );
    }

    @Test
    void testFilterIsApplicable_IntervalNotNull() {
        assertThat(analyticsEventIntervalFilter.isApplicable(filter)).isTrue();
    }

    @Test
    void testFilterIsNotApplicable_IntervalIsNull() {
        filter.setInterval(null);
        assertThat(analyticsEventIntervalFilter.isApplicable(filter)).isFalse();
    }

    @Test
    void testFilterApplySuccess_IntervalNotNull() {
        List<AnalyticsEvent> result = analyticsEventIntervalFilter.apply(events, filter).toList();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result)
                .allMatch(event -> !event.getReceivedAt().isBefore(filter.getInterval().calculateIntervalStart()));
    }

    @Test
    void testFilterApply_NoEventsInInterval() {
        filter.setInterval(Interval.DAY);

        List<AnalyticsEvent> result = analyticsEventIntervalFilter.apply(events, filter).toList();

        assertThat(result).isEmpty();
    }
}