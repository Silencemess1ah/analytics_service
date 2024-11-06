package faang.school.analytics.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentEventDto extends AbstractEventDto {

    @Override
    @JsonProperty("postId")
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    @JsonProperty("authorId")
    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}