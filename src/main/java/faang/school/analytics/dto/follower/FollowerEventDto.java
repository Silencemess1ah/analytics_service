package faang.school.analytics.dto.follower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerEventDto {
    private long followerId;
    private long followeeId;
    private LocalDateTime subscriptionTime;
}
