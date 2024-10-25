package faang.school.analytics.model.event;

import faang.school.analytics.model.enums.EventType;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerEvent {
    private Long followerId;
    private Long followedUserId;
    private Long followedProjectId;
    private LocalDateTime followedAt;
    private EventType eventType = EventType.FOLLOWER;
}