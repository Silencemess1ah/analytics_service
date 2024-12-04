package faang.school.analytics.controller.event;

import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.dto.event.EventRequestDto;
import faang.school.analytics.service.event.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
@Validated
public class EventController {
    private final EventService eventService;

    @PostMapping()
    public EventDto addNewEvent(@Valid @RequestBody EventDto eventDto) {
        return eventService.addNewEvent(eventDto);
    }

    @PostMapping("/get")
    public List<EventDto> getEvents(@Valid @RequestBody EventRequestDto eventRequestDto) {
        return eventService.getEventsDto(eventRequestDto);
    }
}
