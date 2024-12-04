package faang.school.analytics.specification;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AnalyticsEventSpecification {

    public static Specification<AnalyticsEvent> hasReceiverId(Long receiverId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("receiverId"), receiverId);
    }

    public static Specification<AnalyticsEvent> hasEventType(EventType eventType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("eventType"), eventType);
    }

    public static Specification<AnalyticsEvent> withinPeriod(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("receivedAt"), start, end);
            } else if (start != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("receivedAt"), start);
            } else if (end != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("receivedAt"), end);
            }
            return null;
        };
    }
}

