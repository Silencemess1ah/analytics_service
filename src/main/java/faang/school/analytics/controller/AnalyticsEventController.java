package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @PostMapping
    public AnalyticsEventDto saveEvent(@Valid @RequestBody AnalyticsEventDto event) {
        if (event.getId() != null) {
            log.warn("Attempt save event with id");
            throw new DataValidationException("Event don't have id for save");
        }
        log.info("Save event. Type = {}. ReceiverId = {}. ActorId = {}.", event.getEventType(), event.getReceivedAt(), event.getActorId());
        return analyticsEventService.saveEvent(event);
    }

    @GetMapping
    public List<AnalyticsEventDto> getEvents(
            @RequestParam Long receiverId,
            @RequestParam Integer eventType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Integer dayInterval
    ) {
        if (dayInterval != null && (from != null || to != null)) {
            log.warn("Incorrect filter for get events: interval = {}, fromAt = {}, toAt = {}", dayInterval, from, to);
            throw new DataValidationException("Search filter required 'Interval' or 'Dates'");
        }
        AnalyticsEventFilterDto filter = new AnalyticsEventFilterDto(receiverId, eventType, from, to, dayInterval);
        log.info("Requested events with filter: interval = {}, fromAt = {}, toAt = {}",
                filter.getDayInterval(), filter.getFrom(), filter.getTo());
        return analyticsEventService.getAnalytics(filter);
    }
}
