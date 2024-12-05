package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipRequestEvent mentorshipRequestEvent = objectMapper.readValue(message.getBody(), MentorshipRequestEvent.class);
            analyticsEventService.saveMentorshipRequestEvent(mentorshipRequestEvent);
        } catch (IOException e) {
            log.error("Error occurred while deserializing mentorship request event.", e);
            throw new RuntimeException("Failed to deserialize mentorship request event.", e);
        } catch (Exception e) {
            log.error("Unexpected error while processing mentorship request event.", e);
            throw new RuntimeException("Unexpected error while processing mentorship request event.", e);
        }
    }
}
