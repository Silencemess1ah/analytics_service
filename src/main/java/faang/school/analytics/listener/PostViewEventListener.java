package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.event.FollowerEvent;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class PostViewEventListener extends AbstractRedisListener<FollowerEvent> {

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventServiceImpl analyticsEventServiceImpl;

    public PostViewEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventServiceImpl analyticsEventServiceImpl,
                                 AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventServiceImpl);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventServiceImpl = analyticsEventServiceImpl;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(FollowerEvent.class, message, this::mapToAnalyticsEvent);
    }

    private AnalyticsEvent mapToAnalyticsEvent(FollowerEvent followerEvent) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.fromFollowerEventToEntity(followerEvent);
        analyticsEventServiceImpl.saveEvent(analyticsEvent);
        return analyticsEvent;
    }
}