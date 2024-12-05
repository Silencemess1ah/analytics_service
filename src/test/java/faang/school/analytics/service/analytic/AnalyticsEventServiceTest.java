package faang.school.analytics.service.analytic;

import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @Mock
    AnalyticsEventMapper analyticsEventMapper;
    @Mock
    AnalyticsEventRepository analyticsEventRepository;
    @InjectMocks
    AnalyticsEventService analyticsEventService;

    @Test
    void testSaveEvent() {
        Mockito.when(analyticsEventRepository.save(any())).thenReturn(new AnalyticsEvent());
        Mockito.when(analyticsEventMapper.toEntity(any())).thenReturn(provideEvent(1L, LocalDateTime.now()));
        Mockito.when(analyticsEventMapper.toDto(any())).thenReturn(provideEventDto(1L, LocalDateTime.now()));

        AnalyticsEventDto dto = provideEventDto(null, LocalDateTime.now());

        assertDoesNotThrow(() -> analyticsEventService.saveEvent(dto));
        Mockito.verify(analyticsEventMapper, times(1)).toEntity(any());
        Mockito.verify(analyticsEventRepository, times(1)).save(any());
        Mockito.verify(analyticsEventMapper, times(1)).toDto(any());
    }

    @Test
    void testGetEvents() {
        Mockito.when(analyticsEventMapper.toDto(any())).thenReturn(provideEventDto(1L, LocalDateTime.now()));

        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any())).thenReturn(
                Stream.of(
                        provideEvent(1L, LocalDateTime.now()),
                        provideEvent(2L, LocalDateTime.now())
                )
        );

        AnalyticsEventFilterDto filter = AnalyticsEventFilterDto.builder()
                .receiverId(1L)
                .eventType(EventType.FOLLOWER)
                .build();

        List<AnalyticsEventDto> res = analyticsEventService.getAnalytics(filter);

        assertEquals(2, res.size());
        Mockito.verify(analyticsEventMapper, times(2)).toDto(any());
    }

    @Test
    void testFilterByInterval() {
        Stream<AnalyticsEvent> events = Stream.of(
                provideEvent(1L, LocalDateTime.now().minusDays(4)),
                provideEvent(2L, LocalDateTime.now().minusDays(1)),
                provideEvent(3L, LocalDateTime.now().minusDays(2)),
                provideEvent(4L, LocalDateTime.now().minusDays(5))
        );

        Stream<AnalyticsEvent> filteredEvents = analyticsEventService.filterByInterval(events, 3);
        List<AnalyticsEvent> eventsList = filteredEvents.toList();
        assertAll(
                () -> assertEquals(2, eventsList.size()),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 2)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 3))
        );
    }

    @Test
    void testFilterByDates() {
        LocalDateTime now = LocalDateTime.now();

        Stream<AnalyticsEvent> events = Stream.of(
                provideEvent(1L, now.minusDays(5)),
                provideEvent(2L, now),
                provideEvent(3L, now.minusDays(2)),
                provideEvent(4L, now.minusDays(1)),
                provideEvent(5L, now.minusDays(4))
        );

        LocalDateTime from = now.minusDays(4);
        LocalDateTime to = now.minusDays(1);

        Stream<AnalyticsEvent> filteredEvents = analyticsEventService.filterByDates(events, from, to);
        List<AnalyticsEvent> eventsList = filteredEvents.toList();
        assertAll(
                () -> assertEquals(3, eventsList.size()),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 3)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 4)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 5))
        );
    }

    @Test
    void testFilterByDatesWithFromAt() {
        LocalDateTime now = LocalDateTime.now();

        Stream<AnalyticsEvent> events = Stream.of(
                provideEvent(1L, now.minusDays(5)),
                provideEvent(2L, now),
                provideEvent(3L, now.minusDays(2)),
                provideEvent(4L, now.minusDays(1)),
                provideEvent(5L, now.minusDays(4))
        );

        LocalDateTime from = now.minusDays(4);
        LocalDateTime to = null;

        Stream<AnalyticsEvent> filteredEvents = analyticsEventService.filterByDates(events, from, to);
        List<AnalyticsEvent> eventsList = filteredEvents.toList();
        assertAll(
                () -> assertEquals(4, eventsList.size()),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 2)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 3)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 4)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 5))
        );
    }

    @Test
    void testFilterByDatesWithToAt() {
        LocalDateTime now = LocalDateTime.now();

        Stream<AnalyticsEvent> events = Stream.of(
                provideEvent(1L, now.minusDays(5)),
                provideEvent(2L, now),
                provideEvent(3L, now.minusDays(2)),
                provideEvent(4L, now.minusDays(1)),
                provideEvent(5L, now.minusDays(4))
        );

        LocalDateTime from = null;
        LocalDateTime to = now.minusDays(1);

        Stream<AnalyticsEvent> filteredEvents = analyticsEventService.filterByDates(events, from, to);
        List<AnalyticsEvent> eventsList = filteredEvents.toList();
        assertAll(
                () -> assertEquals(4, eventsList.size()),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 1)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 3)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 4)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 5))
        );
    }

    @Test
    void testFilterByDatesWithNullAt() {
        LocalDateTime now = LocalDateTime.now();

        Stream<AnalyticsEvent> events = Stream.of(
                provideEvent(1L, now.minusDays(5)),
                provideEvent(2L, now),
                provideEvent(3L, now.minusDays(2)),
                provideEvent(4L, now.minusDays(1)),
                provideEvent(5L, now.minusDays(4))
        );

        LocalDateTime from = null;
        LocalDateTime to = null;

        Stream<AnalyticsEvent> filteredEvents = analyticsEventService.filterByDates(events, from, to);
        List<AnalyticsEvent> eventsList = filteredEvents.toList();
        assertAll(
                () -> assertEquals(5, eventsList.size()),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 1)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 2)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 3)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 4)),
                () -> assertTrue(eventsList.stream().anyMatch(e -> e.getId() == 5))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDateCases")
    void testCheckDate(LocalDateTime date, LocalDateTime from, LocalDateTime to, boolean expected) {
        boolean res = analyticsEventService.checkDate(date, from, to);
        assertEquals(expected, res);
    }

    private static Stream<Arguments> provideDateCases() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now, now.minusDays(1), now.plusDays(1), true),
                Arguments.of(now, now, now, true),
                Arguments.of(now, null, now, true),
                Arguments.of(now, now, null, true),
                Arguments.of(now, null, null, true),
                Arguments.of(now, now.plusDays(1), now.plusDays(2), false),
                Arguments.of(now, now.plusDays(1), null, false),
                Arguments.of(now, now.minusDays(2), now.minusDays(1), false),
                Arguments.of(now, null, now.minusDays(1), false),
                Arguments.of(now, now.plusDays(1), now.minusDays(1), false)
        );
    }

    private AnalyticsEventDto provideEventDto(Long id, LocalDateTime date) {
        return AnalyticsEventDto.builder()
                .id(id)
                .eventType(1)
                .receiverId(1L)
                .receivedAt(date)
                .actorId(1L)
                .build();
    }

    private AnalyticsEvent provideEvent(Long id, LocalDateTime dateTime) {
        return AnalyticsEvent.builder()
                .id(id)
                .receiverId(1)
                .eventType(EventType.of(1))
                .receivedAt(dateTime)
                .actorId(1)
                .build();
    }

}
