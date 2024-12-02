package faang.school.analytics.service;

import faang.school.analytics.config.Interval;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    public void saveEvent(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
    }

    public List<AnalyticsEventDto> getAnalytics(
            long receiverId,
            EventType eventType,
            Interval interval,
            LocalDateTime from,
            LocalDateTime to
    ) {
        try (Stream<AnalyticsEvent> eventsStream = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)) {
            return eventsStream
                    .filter(event ->
                            interval == null
                                    ? isWithinPeriod(event.getReceivedAt(), from, to)
                                    : interval.contains(event.getReceivedAt())
                    )
                    .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }
    }

    private boolean isWithinPeriod(LocalDateTime date, LocalDateTime from, LocalDateTime to) {
        return (from == null || !date.isBefore(from)) && (to == null || !date.isAfter(to));
    }

    private AnalyticsEventDto toDto(AnalyticsEvent event) {
        return AnalyticsEventDto.builder()
                .id(event.getId())
                .receiverId(event.getReceiverId())
                .actorId(event.getActorId())
                .eventType(event.getEventType())
                .receivedAt(event.getReceivedAt())
                .build();
    }
}
