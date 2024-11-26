package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationEvent {
    private Long recommendationId;
    private Long requesterId;
    private Long receiverId;
    private LocalDateTime eventTime;
}
