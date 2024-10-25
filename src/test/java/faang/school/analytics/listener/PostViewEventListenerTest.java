package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.event.FollowerEvent;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostViewEventListenerTest {

    @InjectMocks
    private PostViewEventListener postViewEventListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventServiceImpl analyticsEventServiceImpl;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnMessage() throws Exception {
        FollowerEvent followerEvent = new FollowerEvent(1L, 2L, null, LocalDateTime.now(), null);
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(objectMapper.readValue(message.getBody(), FollowerEvent.class)).thenReturn(followerEvent);
        when(analyticsEventMapper.fromFollowerEventToEntity(followerEvent)).thenReturn(analyticsEvent);

        postViewEventListener.onMessage(message, null);

        ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventServiceImpl, times(2)).saveEvent(analyticsEventCaptor.capture());

        assertEquals(analyticsEvent, analyticsEventCaptor.getValue());
    }
}