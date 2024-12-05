package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MentorshipRequestEvent {
    private long receiverId;
    private long actorId;
    private LocalDateTime receivedAt;
}
