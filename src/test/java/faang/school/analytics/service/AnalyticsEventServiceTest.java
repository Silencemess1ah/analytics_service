package faang.school.analytics.service;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventFilterDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventResponseDto;
import faang.school.analytics.filter.Filter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private List<Filter<AnalyticsEvent, AnalyticsEventFilterDto>> filters;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent analyticsEvent;
    private AnalyticsEventResponseDto responseDto;
    private Long receiverId;
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;

    @BeforeEach
    void setUp() {
        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.FOLLOWER)
                .build();

        receiverId = 2L;
        eventType = EventType.FOLLOWER;
        interval = Interval.WEEK;
        from = LocalDateTime.now().minusDays(7);
        to = LocalDateTime.now();

        responseDto = AnalyticsEventResponseDto.builder()
                .id(1L)
                .receiverId(receiverId)
                .eventType(eventType)
                .build();
    }

    @Test
    void testSaveEventSuccess() {
        when(analyticsEventRepository.save(analyticsEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    void testGetAnalyticsReturnsCorrectDtoList() {
        Stream<AnalyticsEvent> analyticsEventsStream = Stream.of(analyticsEvent);

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(analyticsEventsStream);
        when(filters.stream()).thenReturn(Stream.of());
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(responseDto);
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(receiverId, eventType);
        verify(analyticsEventMapper, times(1)).entityToResponseDto(analyticsEvent);
    }

    @Test
    void testGetAnalyticsReturnsEmptyListWhenNoEvents() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(Stream.empty());
        when(filters.stream()).thenReturn(Stream.empty());

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);

        assertThat(result).isEmpty();
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(receiverId, eventType);
    }
}