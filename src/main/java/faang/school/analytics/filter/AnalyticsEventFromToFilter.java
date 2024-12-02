package faang.school.analytics.filter;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AnalyticsEventFromToFilter implements Filter<AnalyticsEvent, AnalyticsEventFilterDto> {

    @Override
    public boolean isApplicable(AnalyticsEventFilterDto filters) {
        boolean isIntervalNull = filters.getInterval() == null;
        boolean isFromOrToPresent = filters.getFrom() != null || filters.getTo() != null;

        return isIntervalNull && isFromOrToPresent;
    }

    @Override
    public Stream<AnalyticsEvent> apply(Stream<AnalyticsEvent> events, AnalyticsEventFilterDto filters) {
        if (filters.getFrom() != null) {
            events = events.filter(event -> !event.getReceivedAt().isBefore(filters.getFrom()));
        }

        if (filters.getTo() != null) {
            events = events.filter(event -> !event.getReceivedAt().isAfter(filters.getTo()));
        }

        return events;
    }
}
