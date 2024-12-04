package faang.school.analytics.service.event;

import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.mapper.event.EventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.analytic_event.AnalyticEventServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final EventMapper eventMapper;
    private final AnalyticEventServiceValidator analyticEventServiceValidator;

    public EventDto addNewEvent(EventDto eventDto) {
        log.info("mapping dto to entity");
        AnalyticsEvent analyticsEvent = eventMapper.toEntity(eventDto);

        if (analyticsEvent.getReceivedAt() == null) {
            log.info("set ReceivedAt now");
            analyticsEvent.setReceivedAt(LocalDateTime.now());
        }
        log.info("save entity in db");
        analyticsEventRepository.save(analyticsEvent);

        log.info("mapping entity to dto");
        return eventMapper.toDto(analyticsEvent);
    }

    public EventDto addNewEvent(AnalyticsEvent analyticsEvent) {
        log.info("validate analyticEvent");
        analyticEventServiceValidator.checkEntity(analyticsEvent);

        if (analyticsEvent.getReceivedAt() == null) {
            log.info("set ReceivedAt now");
            analyticsEvent.setReceivedAt(LocalDateTime.now());
        }

        log.info("save entity in db");
        analyticsEventRepository.save(analyticsEvent);

        log.info("mapping entity to dto");
        return eventMapper.toDto(analyticsEvent);
    }

    @Transactional(readOnly = true)
    public List<EventDto> getEventsDto(long receiverId,
                                       String eventTypeStr,
                                       String intervalStr,
                                       String fromStr,
                                       String toStr) {
        log.info("validate requestParam");
        analyticEventServiceValidator.validateInterval(intervalStr, fromStr, toStr);
        analyticEventServiceValidator.checkIdAndEvent(receiverId, eventTypeStr);

        EventType eventType = getEventType(eventTypeStr);
        LocalDateTime from = getLocalDateTime(fromStr);
        LocalDateTime to = getLocalDateTime(toStr);
        Interval interval = getInterval(intervalStr);

        if (interval != null) {
            log.info("changing localDateTime fromStr and toStr as intervalStr");
            from = getTime(interval);
            to = LocalDateTime.now();
        }

        log.info("getting stream of event fromStr db");
        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        LocalDateTime finalFrom = from;
        LocalDateTime finalTo = to;
        log.info("apply filters and sort by received time and then mapping toStr dto");
        return events.filter(analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(finalFrom))
                .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isBefore(finalTo))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnalyticsEvent> getEventsEntity(long receiverId,
                                                String eventTypeStr,
                                                String intervalStr,
                                                String fromStr,
                                                String toStr) {
        log.info("validate eventRequestDto");
        analyticEventServiceValidator.validateInterval(intervalStr, fromStr, toStr);
        analyticEventServiceValidator.checkIdAndEvent(receiverId, eventTypeStr);

        EventType eventType = getEventType(eventTypeStr);
        LocalDateTime from = getLocalDateTime(fromStr);
        LocalDateTime to = getLocalDateTime(toStr);
        Interval interval = getInterval(intervalStr);


        log.info("getting stream of event from db");
        Stream<AnalyticsEvent> events = analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType);

        if (interval != null) {
            log.info("changing localDateTime from and to as interval");
            from = getTime(interval);
            to = LocalDateTime.now();
        }

        LocalDateTime finalFrom = from;
        LocalDateTime finalTo = to;

        log.info("apply filters and sort by received time");
        return events.filter(analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(finalFrom))
                .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isBefore(finalTo))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .collect(Collectors.toList());
    }

    private EventType getEventType(String event) {
        try {
            int id = Integer.parseInt(event);
            return EventType.of(id);
        } catch (NumberFormatException e) {
            try {
                return EventType.valueOf(event.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Такого эвента не существует");
            }
        }
    }

    private Interval getInterval(String interval) {
        if (interval == null || interval.isBlank()) {
            return null;
        }
        try {
            int id = Integer.parseInt(interval);
            return Interval.values()[id];
        } catch (NumberFormatException e) {
            try {
                return Interval.valueOf(interval.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Такого интервала не существует");
            }
        }
    }

    private LocalDateTime getLocalDateTime(String time) {
        if (time == null || time.isBlank()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return LocalDateTime.parse(time, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Не правильно введенное время");
        }
    }

    private LocalDateTime getTime(Interval interval) {
        switch (interval) {
            case DAY -> {
                return LocalDateTime.now().minusDays(1);
            }
            case WEEK -> {
                return LocalDateTime.now().minusWeeks(1);
            }
            case MONTH -> {
                return LocalDateTime.now().minusMonths(1);
            }
            default -> {
                return LocalDateTime.now().minusYears(1);
            }
        }
    }
}
