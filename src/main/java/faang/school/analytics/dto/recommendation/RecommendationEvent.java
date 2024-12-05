package faang.school.analytics.dto.recommendation;

import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecommendationEvent {
    private Long id;
    private Long authorId;
    private Long receiverId;
    private LocalDateTime createdAt;

    public AnalyticsEventDto toAnalyticsEventDto () {
        return new AnalyticsEventDto(id, receiverId, authorId, 8, createdAt);
    }
}
