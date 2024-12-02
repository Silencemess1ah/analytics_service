package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class SearchAppearanceEventListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    public SearchAppearanceEventListener(AnalyticsEventService analyticsEventService, ObjectMapper objectMapper) {
        this.analyticsEventService = analyticsEventService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            SearchAppearanceEvent event = objectMapper.readValue(message.getBody(), SearchAppearanceEvent.class);
            analyticsEventService.processEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}