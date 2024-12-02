package faang.school.analytics.service;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventFilterDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventResponseDto;
import faang.school.analytics.filter.Filter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final List<Filter<AnalyticsEvent, AnalyticsEventFilterDto>> filters;

    public void saveEvent(AnalyticsEvent event) {
        AnalyticsEvent savedEvent = analyticsEventRepository.save(event);
        log.info("Analytics event {} saved successfully.", savedEvent.getId());
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEventResponseDto> getAnalytics(
            Long receiverId, EventType eventType, Interval interval, LocalDateTime from, LocalDateTime to) {

        AnalyticsEventFilterDto filters = AnalyticsEventFilterDto.builder()
                .interval(interval)
                .from(from)
                .to(to)
                .build();

        Stream<AnalyticsEvent> analyticsEvents = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);
        List<AnalyticsEventResponseDto> result = this.filters.stream()
                .filter(filter -> filter.isApplicable(filters))
                .reduce(analyticsEvents, (analyticsEventStream, filter) -> filter.apply(analyticsEventStream, filters),
                        (newStream, oldStream) -> newStream)
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(analyticsEventMapper::entityToResponseDto)
                .toList();

        log.info("Analytics retrieved successfully for receiverId {}", receiverId);

        return result;
    }
}
