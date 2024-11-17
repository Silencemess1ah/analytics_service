package faang.school.analytics.scheduler.event;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import faang.school.analytics.scheduler.ScheduledRatingUpdater;
import faang.school.analytics.service.analytic.AverageValueOfActionCalculator;
import faang.school.analytics.service.analytic.StandardDeviationCalculator;
import faang.school.analytics.service.user.UserRankUpdaterService;
import faang.school.analytics.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduledRatingUpdaterTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AverageValueOfActionCalculator averageValueOfActionCalculator;

    @Mock
    private StandardDeviationCalculator standardDeviationCalculator;

    @Mock
    private UserRankUpdaterService userRankUpdaterService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ScheduledRatingUpdater scheduledRatingUpdater;

    @Captor
    private ArgumentCaptor<Map<Long, Double>> userRankCaptor;

    @Test
    void updateUserRankScore_shouldCalledUpdateUserRankScore() {
        when(analyticsEventRepository.findUserActionCountByEventTypeAndTimeLimit(Mockito.anyString()))
                .thenReturn(Optional.empty());
        when(userService.getUsersSize())
                .thenThrow(new IllegalArgumentException("users size is null"));

        assertThrows(IllegalArgumentException.class, () -> scheduledRatingUpdater.updateUserRankScore());
    }

    @Test
    void calculateZScore_shouldReturnDefaultValueOne() {
        EventType testEventType = EventType.FOLLOWER;
        List<AnalyticsEvent> mockEvents = List.of(AnalyticsEvent.builder()
                .id(1L)
                .eventType(testEventType)
                .build());
        EventType[] eventTypes = new EventType[1];
        eventTypes[0] = EventType.FOLLOWER;
        when(EventType.values())
                .thenReturn(eventTypes);
        when(analyticsEventRepository.findUserActionCountByEventTypeAndTimeLimit(testEventType.name()))
                .thenReturn(Optional.of(mockEvents));
        when(userService.getUsersSize()).thenReturn(1);
        when(averageValueOfActionCalculator.calculate(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(1.0);
        when(standardDeviationCalculator.calculate(Mockito.anyList(), Mockito.anyDouble(), Mockito.anyInt()))
                .thenReturn(1.0);


        scheduledRatingUpdater.updateUserRankScore();

        verify(scheduledRatingUpdater, times(1))
                .calculateZScore(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt(), EventType.FOLLOWER.name());
    }

    @Test
    void updateUserRankScore_shouldHandleZeroEventWeight() {
        // Arrange
        EventType testEventType = EventType.FOLLOWER;
        when(analyticsEventRepository.findUserActionCountByEventTypeAndTimeLimit(testEventType.name()))
                .thenReturn(Optional.of(List.of(AnalyticsEvent.builder()
                        .id(1L)
                        .eventType(testEventType)
                        .build())));
        when(userService.getUsersSize()).thenReturn(1);
        when(averageValueOfActionCalculator.calculate(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(1.0);
        when(standardDeviationCalculator.calculate(Mockito.anyList(), Mockito.anyDouble(), Mockito.anyInt()))
                .thenReturn(1.0);
        when(EventType.getWeightByName(testEventType.name())).thenReturn(0.0);

        // Act
        scheduledRatingUpdater.updateUserRankScore();

        // Assert
        verify(userRankUpdaterService).updateUsersRankInBatch(userRankCaptor.capture());
        Map<Long, Double> capturedRanks = userRankCaptor.getValue();
        assertEquals(0.0, capturedRanks.get(1L), "Rank should be 0 when event weight is 0");
    }

    @Test
    void updateUserRankScore_shouldLogErrorIfExceptionOccurs() {
        // Arrange
        EventType testEventType = EventType.FOLLOWER;
        when(analyticsEventRepository.findUserActionCountByEventTypeAndTimeLimit(testEventType.name()))
                .thenThrow(new RuntimeException("Test exception"));

        // Act
        Assertions.assertDoesNotThrow(() -> scheduledRatingUpdater.updateUserRankScore());

        // Verify no updates are sent to the rank updater service
        verify(userRankUpdaterService, Mockito.never()).updateUsersRankInBatch(Mockito.any());
    }

    @Test
    void calculateZScore_shouldHandleEdgeCases() {
        // Arrange
        double average = 2.0;
        double stdDev = 0.0;
        int userActions = 2;

        // Act & Assert
        assertThrows(ArithmeticException.class,
                () -> scheduledRatingUpdater.calculateZScore(average, stdDev, userActions, EventType.FOLLOWER.name()),
                "Division by zero should throw an exception");
    }

    @Test
    void updateUserRankScore_shouldHandleAsynchronousExecution() throws InterruptedException {
        // Arrange
        EventType testEventType = EventType.FOLLOWER;
        when(analyticsEventRepository.findUserActionCountByEventTypeAndTimeLimit(testEventType.name()))
                .thenReturn(Optional.of(List.of(AnalyticsEvent.builder()
                        .id(1L)
                        .eventType(testEventType)
                        .build())));
        when(userService.getUsersSize()).thenReturn(1);
        when(averageValueOfActionCalculator.calculate(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(1.0);
        when(standardDeviationCalculator.calculate(Mockito.anyList(), Mockito.anyDouble(), Mockito.anyInt()))
                .thenReturn(1.0);
        when(EventType.getWeightByName(testEventType.name())).thenReturn(1.0);

        // Act
        scheduledRatingUpdater.updateUserRankScore();

        // Wait to ensure async method finishes
        Thread.sleep(1000);

        // Assert
        verify(userRankUpdaterService).updateUsersRankInBatch(Mockito.anyMap());
    }
}


