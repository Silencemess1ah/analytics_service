package faang.school.analytics.service;

import faang.school.analytics.Validator.AnalyticsEventValidator;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventResponseDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    private static final int WEEKS_COUNT = 1;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsEventValidator analyticsEventValidator;

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
        receiverId = 2L;
        eventType = EventType.FOLLOWER;
        interval = Interval.WEEK;
        to = LocalDateTime.now();
        from = to.minusWeeks(WEEKS_COUNT);

        analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(receiverId)
                .actorId(3L)
                .eventType(eventType)
                .build();

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
    void testGetAnalyticsWithInterval() {
        when(analyticsEventRepository.findAll(ArgumentMatchers.<Specification<AnalyticsEvent>>any()))
                .thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertThat(result).containsExactly(responseDto);
        verify(analyticsEventValidator, never()).validateDateOrder(from, to);
        verify(analyticsEventRepository, times(1)).findAll(ArgumentMatchers.<Specification<AnalyticsEvent>>any());
        verify(analyticsEventMapper, times(1)).entityToResponseDto(analyticsEvent);
    }

    @Test
    void testGetAnalyticsWithFromAndTo() {
        interval = null;

        when(analyticsEventRepository.findAll(ArgumentMatchers.<Specification<AnalyticsEvent>>any()))
                .thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertWhenIntervalIsNull(result);
    }

    @Test
    void testGetAnalyticsWithFromOnly() {
        interval = null;
        to = null;

        when(analyticsEventRepository.findAll(ArgumentMatchers.<Specification<AnalyticsEvent>>any()))
                .thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertWhenIntervalIsNull(result);
    }

    @Test
    void testGetAnalyticsWithToOnly() {
        interval = null;
        from = null;

        when(analyticsEventRepository.findAll(ArgumentMatchers.<Specification<AnalyticsEvent>>any()))
                .thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertWhenIntervalIsNull(result);
    }

    @Test
    void testGetAnalyticsWithAllNull() {
        interval = null;
        from = null;
        to = null;

        when(analyticsEventRepository.findAll(ArgumentMatchers.<Specification<AnalyticsEvent>>any()))
                .thenReturn(List.of(analyticsEvent));
        when(analyticsEventMapper.entityToResponseDto(analyticsEvent)).thenReturn(responseDto);

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to);

        assertWhenIntervalIsNull(result);
    }

    private void assertWhenIntervalIsNull(List<AnalyticsEventResponseDto> result) {
        assertThat(result).containsExactly(responseDto);
        verify(analyticsEventValidator, times(1)).validateDateOrder(from, to);
        verify(analyticsEventRepository, times(1)).findAll(ArgumentMatchers.<Specification<AnalyticsEvent>>any());
        verify(analyticsEventMapper, times(1)).entityToResponseDto(analyticsEvent);
    }
}