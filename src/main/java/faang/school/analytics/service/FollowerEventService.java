package faang.school.analytics.service;

import faang.school.analytics.model.event.FollowerEvent;

public interface FollowerEventService {
    void handleFollowerEvent(FollowerEvent followerEvent);
}