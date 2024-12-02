package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.filter.AnalyticsFilterI;
import faang.school.analytics.mappers.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final List<AnalyticsFilterI> analyticsFilters;

    public void saveEvent(AnalyticsEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
        analyticsEventRepository.save(analyticsEvent);
    }

    public List<AnalyticsEventDto> getAnalytics(AnalyticsFilterDto analyticsFilterDto) {
        try (Stream<AnalyticsEvent> eventsStream = analyticsEventRepository.findByReceiverIdAndEventType(analyticsFilterDto.getReceiverId(),
                analyticsFilterDto.getEventType())) {

            Stream<AnalyticsEvent> filteredStream = eventsStream;
            for (AnalyticsFilterI filter : analyticsFilters) {
                if (filter.isApplicable(analyticsFilterDto)) {
                    filteredStream = filter.apply(filteredStream, analyticsFilterDto);
                }
            }

            return filteredStream
                    .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                    .map(analyticsEventMapper::toDto)
                    .collect(Collectors.toList());
        }
    }
}