package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.service.AnalyticService;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationEventListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    @EventListener
    public void handleMessage(String jsonEvent) {
        log.info("Event processing: {}", jsonEvent);
        RecommendationEvent event = readEvent(jsonEvent);
        analyticsEventService.saveRecommendationEvent(event);
        log.info("Event processed: {}", event);
    }

    private RecommendationEvent readEvent(String jsonEvent) {
        try {
            return objectMapper.readValue(jsonEvent, RecommendationEvent.class);
        } catch (JsonProcessingException exception) {
            log.error("Error parsing json event: {}", jsonEvent, exception);
            throw new RuntimeException(exception);
        }
    }
}
