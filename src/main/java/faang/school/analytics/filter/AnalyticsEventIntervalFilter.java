package faang.school.analytics.filter;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;
import java.util.stream.Stream;

@Component
public class AnalyticsEventIntervalFilter implements Filter<AnalyticsEvent, AnalyticsEventFilterDto> {

    @Override
    public boolean isApplicable(AnalyticsEventFilterDto filters) {
        return filters.getInterval() != null;
    }

    @Override
    public Stream<AnalyticsEvent> apply(Stream<AnalyticsEvent> events, AnalyticsEventFilterDto filters) {
        return events.filter(event ->
                !event.getReceivedAt().isBefore(filters.getInterval().calculateIntervalStart()));
    }
}