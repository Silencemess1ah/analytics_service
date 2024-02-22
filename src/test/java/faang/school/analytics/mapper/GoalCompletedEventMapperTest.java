package faang.school.analytics.mapper;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class GoalCompletedEventMapperTest {
    private final GoalCompletedEventMapper goalCompletedEventMapper = Mappers.getMapper(GoalCompletedEventMapper.class);
    @Test
    void testToAnalyticsEvent() {
        GoalCompletedEvent goalCompletedEvent = GoalCompletedEvent.builder().build();
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder().eventType(EventType.GOAL_COMPLETED).build();
        assertEquals(analyticsEvent,goalCompletedEventMapper.toAnalyticsEvent(goalCompletedEvent));
    }
}