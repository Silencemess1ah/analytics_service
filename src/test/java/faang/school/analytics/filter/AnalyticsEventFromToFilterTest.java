package faang.school.analytics.filter;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsEventFromToFilterTest {
    private AnalyticsEventFromToFilter analyticsEventFromToFilter;
    private AnalyticsEventFilterDto filter;
    private Stream<AnalyticsEvent> events;

    @BeforeEach
    void setUp() {
        analyticsEventFromToFilter = new AnalyticsEventFromToFilter();
        filter = AnalyticsEventFilterDto.builder()
                .from(LocalDateTime.now(ZoneId.of("UTC")).minus(Duration.ofDays(2)))
                .to(LocalDateTime.now(ZoneId.of("UTC")))
                .build();

        events = Stream.of(
                AnalyticsEvent.builder()
                        .receivedAt(LocalDateTime.now(ZoneId.of("UTC")).minus(Duration.ofDays(1)))
                        .build(),
                AnalyticsEvent.builder()
                        .receivedAt(LocalDateTime.now(ZoneId.of("UTC")).minus(Duration.ofDays(3)))
                        .build()
        );
    }

    @Test
    void testFilterIsNotApplicable() {
        filter.setFrom(null);
        filter.setTo(null);

        assertThat(analyticsEventFromToFilter.isApplicable(filter)).isFalse();
    }

    @Test
    void testFilterIsApplicable_FromIsNull() {
        filter.setFrom(null);

        assertThat(analyticsEventFromToFilter.isApplicable(filter)).isTrue();
    }

    @Test
    void testFilterIsApplicable_ToIsNull() {
        filter.setTo(null);

        assertThat(analyticsEventFromToFilter.isApplicable(filter)).isTrue();
    }

    @Test
    void testFilterApplySuccess_FromIsNull() {
        filter.setFrom(null);

        List<AnalyticsEvent> result = analyticsEventFromToFilter.apply(events, filter).toList();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .allMatch(event -> event.getReceivedAt().isBefore(filter.getTo()));
    }

    @Test
    void testFilterApplySuccess_ToIsNull() {
        filter.setTo(null);

        List<AnalyticsEvent> result = analyticsEventFromToFilter.apply(events, filter).toList();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result)
                .allMatch(event -> event.getReceivedAt().isAfter(filter.getFrom()));
    }

    @Test
    void testFilterApplySuccess_FromToNotNull() {
        List<AnalyticsEvent> result = analyticsEventFromToFilter.apply(events, filter).toList();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result).allMatch(event ->
                event.getReceivedAt().isAfter(filter.getFrom()) && event.getReceivedAt().isBefore(filter.getTo()));
    }
}