package faang.school.analytics.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEventFilterDto {
    @NotNull
    private Long receiverId;

    @NotNull
    private Integer eventType;

    @Nullable
    private LocalDateTime from;

    @Nullable
    private LocalDateTime to;

    @Nullable
    private Integer dayInterval;

}
