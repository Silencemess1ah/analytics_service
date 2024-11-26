package faang.school.analytics.service;

import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.model.LikeEvent;

public interface AnalyticsEventService {
    void saveLikeEvent(LikeEvent likeEvent);
    void saveRecommendationEvent(RecommendationEvent recommendationEvent);
}
