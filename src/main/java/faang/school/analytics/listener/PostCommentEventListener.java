package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component("postCommentEventListener")
public class PostCommentEventListener extends AbstractEventListener<CommentEventDto> {

    public PostCommentEventListener(ObjectMapper objectMapper, AnalyticsEventMapper analyticsEventMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEventDto commentEventDto = handleEvent(message, CommentEventDto.class);
        log.debug("Received event: {}", commentEventDto);
        sendAnalytics(commentEventDto);
        log.debug("Sent event: {}", commentEventDto);
    }

    @Override
    protected EventType getEventType() {
        return EventType.POST_COMMENT;
    }
}