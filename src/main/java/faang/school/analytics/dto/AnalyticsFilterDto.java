package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AnalyticsFilterDto {
    private long receiverId;
    private EventType eventType;
    private LocalDateTime from;
    private LocalDateTime to;
}
