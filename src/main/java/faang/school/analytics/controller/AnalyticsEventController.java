package faang.school.analytics.controller;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventResponseDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/analytics-events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AnalyticsEventController {
    private final AnalyticsEventService analyticsEventService;

    @GetMapping
    public ResponseEntity<List<AnalyticsEventResponseDto>> getAnalytics(
            @RequestParam(name = "receiverId")
            @Positive(message = "ReceiverId must be a positive value,") Long receiverId,
            @RequestParam(name = "eventType")
            @NotNull(message = "EventType is required.") EventType eventType,
            @RequestParam(required = false) Interval interval,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to
    ) {
        log.info("Requesting analytics for receiverId {}, event type: '{}'.", receiverId, eventType);

        return ResponseEntity.ok(analyticsEventService.getAnalytics(
                receiverId, eventType, interval, from, to));
    }
}
