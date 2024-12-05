package faang.school.analytics.mapper;

import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {
    AnalyticsEventDto toDto(AnalyticsEvent event);

    AnalyticsEvent toEntity(AnalyticsEventDto eventDto);

    default Integer map(EventType type) {
        return type.ordinal();
    }

    default EventType map(Integer value) {
        return EventType.of(value);
    }

}
