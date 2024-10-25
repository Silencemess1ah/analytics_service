package faang.school.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.PostViewEvent;
import faang.school.analytics.exception.DeserializeException;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.listener.PostViewEventListener;
import faang.school.analytics.mapper.PostViewEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostViewEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PostViewEventMapper postViewEventMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private PostViewEventListener postViewEventListener;

    private PostViewEvent postViewEvent;
    private Message message;

    @BeforeEach
    public void setUp() throws Exception {
        Field objectMapperField = AbstractEventListener.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(postViewEventListener, objectMapper);

        postViewEvent = new PostViewEvent(1L, 2L, 3L, LocalDateTime.now());
        message = mock(Message.class);
    }

    @Test
    public void testOnMessageCallsSaveEventWhenDeserializationSucceeds() throws IOException {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        analyticsEvent.setEventType(EventType.POST_VIEW);

        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(PostViewEvent.class))).thenReturn(postViewEvent);
        when(postViewEventMapper.toAnalyticsEvent(postViewEvent)).thenReturn(analyticsEvent);

        postViewEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(PostViewEvent.class));
        verify(postViewEventMapper, times(1)).toAnalyticsEvent(postViewEvent);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }

    @Test
    public void testOnMessageThrowsDeserializeExceptionWhenDeserializationFails() throws IOException {
        when(message.getBody()).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(PostViewEvent.class))).thenThrow(new IOException("Deserialization failed"));

        assertThrows(DeserializeException.class, () -> postViewEventListener.onMessage(message, null));

        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(PostViewEvent.class));
        verifyNoInteractions(postViewEventMapper, analyticsEventService);
    }
}


