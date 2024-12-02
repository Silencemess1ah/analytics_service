package faang.school.analytics.dto.analyticsEvent;

import faang.school.analytics.model.Interval;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticsEventFilterDto {
    Interval interval;
    LocalDateTime from;
    LocalDateTime to;
}
