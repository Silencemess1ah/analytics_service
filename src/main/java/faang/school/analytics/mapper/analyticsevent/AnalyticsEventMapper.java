package faang.school.analytics.mapper.analyticsevent;

import faang.school.analytics.model.dto.LikeEvent;
import faang.school.analytics.model.dto.event.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.event.CommentEvent;
import faang.school.analytics.model.event.FollowerEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(target = "receiverId", source = "followeeId")
    @Mapping(target = "actorId", source = "followerId")
    @Mapping(target = "receivedAt", source = "subscribedAt")
    AnalyticsEvent toEntity(FollowerEvent followerEvent);

    @Mapping(target = "receiverId", source = "postId")
    @Mapping(target = "actorId", source = "userId")
    @Mapping(target = "receivedAt", source = "likedTime")
    AnalyticsEvent toEntity(LikeEvent likeEvent);

    @Mapping(target = "receiverId", source = "commentId")
    @Mapping(target = "actorId", source = "authorId")
    @Mapping(target = "receivedAt", source = "commentedAt")
    AnalyticsEvent toEntity(CommentEvent commentEvent);
}
