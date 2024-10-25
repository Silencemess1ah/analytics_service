package faang.school.analytics.controller;

import faang.school.analytics.model.event.FollowerEvent;
import faang.school.analytics.service.FollowerEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsEventController {

    private final FollowerEventService followerEventService;

    @PostMapping("/follower-event")
    public ResponseEntity<String> receiveFollowerEvent(@RequestBody FollowerEvent followerEvent) {
        log.info("Received follower event: {}", followerEvent);
        try {
            followerEventService.handleFollowerEvent(followerEvent);
            return ResponseEntity.ok("Follower event saved successfully.");
        } catch (Exception e) {
            log.error("Error saving follower event: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Failed to save follower event: " + e.getMessage());
        }
    }
}