package faang.school.analytics.service;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.interval.Interval;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsEventService {
    AnalyticsEventDto save(AnalyticsEventDto analyticsEventDto);
    List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to);
}
