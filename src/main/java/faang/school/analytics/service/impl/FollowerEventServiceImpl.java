package faang.school.analytics.service.impl;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.event.FollowerEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.FollowerEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowerEventServiceImpl implements FollowerEventService {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper mapper;

    @Override
    public void handleFollowerEvent(FollowerEvent followerEvent) {
        AnalyticsEvent analyticsEvent = mapper.fromFollowerEventToEntity(followerEvent);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}