package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MentorshipRequestedEventDto extends AbstractEventDto {

    @Override
    @JsonProperty("receiverId")
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("actorId")
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    @JsonProperty("timestamp")
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
