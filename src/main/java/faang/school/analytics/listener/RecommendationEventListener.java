package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.handler.EventHandler;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RecommendationEventListener extends AbstractEventListener<RecommendationEvent> implements MessageListener {

    public RecommendationEventListener(ObjectMapper objectMapper, List<EventHandler<RecommendationEvent>> eventHandlers, AnalyticsEventService analyticsEventService) {
        super(objectMapper, eventHandlers, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationEvent.class, analyticsEventService::saveRecommendationEvent);
    }
}
