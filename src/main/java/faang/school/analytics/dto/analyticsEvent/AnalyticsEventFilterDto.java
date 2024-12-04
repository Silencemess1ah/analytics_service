package faang.school.analytics.dto.analyticsEvent;

import faang.school.analytics.model.Interval;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsEventFilterDto {
    Interval interval;
    LocalDateTime from;
    LocalDateTime to;
}
