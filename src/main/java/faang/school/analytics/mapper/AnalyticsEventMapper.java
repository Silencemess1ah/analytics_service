package faang.school.analytics.mapper;

import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventResponseDto;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventResponseDto entityToResponseDto(AnalyticsEvent event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventType", constant = "RECOMMENDATION_RECEIVED")
    AnalyticsEvent toAnalyticsEventMentorshipRequest(MentorshipRequestEvent mentorshipRequestEvent);
}
