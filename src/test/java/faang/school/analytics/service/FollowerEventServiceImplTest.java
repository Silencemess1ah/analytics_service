package faang.school.analytics.service;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.event.FollowerEvent;
import faang.school.analytics.service.impl.FollowerEventServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class FollowerEventServiceImplTest {

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventMapper mapper;

    @InjectMocks
    private FollowerEventServiceImpl followerEventService;

    public FollowerEventServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleFollowerEvent_shouldCallSaveEvent() {
        FollowerEvent followerEvent = new FollowerEvent();

        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(mapper.fromFollowerEventToEntity(followerEvent)).thenReturn(analyticsEvent);

        followerEventService.handleFollowerEvent(followerEvent);

        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }
}