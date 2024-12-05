package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.dto.recommendation.RecommendationEvent;
import faang.school.analytics.service.analytic.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RecommendationEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent event = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            analyticsEventService.saveEvent(event.toAnalyticsEventDto());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
