package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationEventListenerTest {
    @InjectMocks
    RecommendationEventListener recommendationEventListener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    private RecommendationEvent event;
    private String jsonEvent;

    @BeforeEach
    void setUp() {
        event = new RecommendationEvent();
        jsonEvent = "json";
    }

    @Test
    void handleMessageTest() throws JsonProcessingException {
        when(objectMapper.readValue(jsonEvent, RecommendationEvent.class)).thenReturn(event);

        recommendationEventListener.handleMessage(jsonEvent);

        verify(analyticsEventService, times(1)).saveRecommendationEvent(event);
    }
}
