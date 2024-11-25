package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestedEventDto> {

    public MentorshipRequestedEventListener(ObjectMapper objectMapper,
                                            AnalyticsEventMapper analyticsEventMapper,
                                            AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    protected EventType getEventType() {
        return EventType.MENTORSHIP_REQUESTED;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipRequestedEventDto event = handleEvent(message, MentorshipRequestedEventDto.class);
            log.debug("Received mentorship requested event: {}", event);
            sendAnalytics(event);
        } catch (RuntimeException e) {
            log.error("Failed to process mentorship requested event: {}", e.getMessage());
        }
    }
}