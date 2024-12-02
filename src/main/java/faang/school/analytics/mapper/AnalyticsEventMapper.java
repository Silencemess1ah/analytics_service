package faang.school.analytics.mapper;

import faang.school.analytics.dto.analytic.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    @Mapping(source = "eventTypeNumber", target = "eventType", qualifiedByName = "mapToEventType")
    AnalyticsEvent toEntity(AnalyticsEventDto analyticsEventDto);

    @Mapping(source = "eventType", target = "eventTypeNumber", qualifiedByName = "mapToEventTypeNumber")
    AnalyticsEventDto toDto(AnalyticsEvent analyticsEvent);

    @Named("mapToEventTypeNumber")
    default int map(EventType eventType) {
        return eventType.ordinal();
    }

    @Named("mapToEventType")
    default EventType map(int eventTypeNumber) {
        return EventType.of(eventTypeNumber);
    }
}
