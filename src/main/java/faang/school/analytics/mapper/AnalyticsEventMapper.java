package faang.school.analytics.mapper;

import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.dto.AdBoughtEvent;
import faang.school.analytics.model.dto.FundRaisedEvent;
import faang.school.analytics.model.dto.ProfileViewEvent;
import faang.school.analytics.model.dto.SearchAppearanceEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.event.FollowerEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEvent toEntity(AnalyticsEventDto analyticEventDto);

    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    AnalyticsEvent fromSearchAppearanceToEntity(SearchAppearanceEvent searchAppearanceEvent);

    AnalyticsEvent fromProfileViewToEntity(ProfileViewEvent profileViewEvent);

    @Mapping(source = "userId", target = "actorId")
    AnalyticsEvent fromAdBoughtToEntity(AdBoughtEvent adBoughtEvent);

    @Mapping(source = "projectId", target = "receiverId")
    AnalyticsEvent fromFundRaisedToEntity(FundRaisedEvent fundRaisedEvent);

    @Mapping(source = "followerId", target = "actorId")
    @Mapping(source = "followedUserId", target = "receiverId")
    @Mapping(source = "eventType", target = "eventType")
    @Mapping(source = "followedAt", target = "receivedAt")
    AnalyticsEvent fromFollowerEventToEntity(FollowerEvent followerEvent);
}