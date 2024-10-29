package faang.school.analytics.listener;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestedEventListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private MentorshipRequestedEventListener mentorshipRequestedEventListener;

    private Message message;
    private MentorshipRequestedEventDto mentorshipRequestedEvent;

    @BeforeEach
    public void setUp() {
        String eventJson = "{\"receiverId\":2,\"actorId\":1,\"timestamp\":\"2024-10-17T12:34:56\"}";
        message = new DefaultMessage("mentorship_requested_channel".getBytes(), eventJson.getBytes());
        mentorshipRequestedEvent = new MentorshipRequestedEventDto();
        mentorshipRequestedEvent.setReceiverId(2L);
        mentorshipRequestedEvent.setActorId(1L);
        mentorshipRequestedEvent.setTimestamp(LocalDateTime.of(2024, 10, 17, 12, 34, 56));
    }

    @Test
    public void testHandleEventSuccess() throws IOException {
        when(objectMapper.readValue(message.getBody(), MentorshipRequestedEventDto.class))
                .thenReturn(mentorshipRequestedEvent);

        MentorshipRequestedEventDto event = mentorshipRequestedEventListener.handleEvent(message, MentorshipRequestedEventDto.class);

        assertNotNull(event);
        assertEquals(mentorshipRequestedEvent.getReceiverId(), event.getReceiverId());
        assertEquals(mentorshipRequestedEvent.getActorId(), event.getActorId());
        assertEquals(mentorshipRequestedEvent.getTimestamp(), event.getTimestamp());
    }

    @Test
    public void testHandleEventFailure() throws IOException {
        when(objectMapper.readValue(message.getBody(), MentorshipRequestedEventDto.class))
                .thenThrow(new IOException("Deserialization error"));

        assertThrows(RuntimeException.class, () -> {
            mentorshipRequestedEventListener.handleEvent(message, MentorshipRequestedEventDto.class);
        });
    }

    @Test
    public void testSendAnalyticsSuccess() {
        mentorshipRequestedEventListener.sendAnalytics(mentorshipRequestedEvent);

        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEvent.class));
    }

    @Test
    public void testOnMessage() throws IOException {
        when(objectMapper.readValue(message.getBody(), MentorshipRequestedEventDto.class))
                .thenReturn(mentorshipRequestedEvent);

        mentorshipRequestedEventListener.onMessage(message, null);

        verify(objectMapper, times(1)).readValue(message.getBody(), MentorshipRequestedEventDto.class);
        verify(analyticsEventService, times(1)).saveEvent(any(AnalyticsEvent.class));
    }
}