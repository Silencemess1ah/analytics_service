package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticController {

    public final AnalyticsEventService analyticsEventService;

    @PostMapping("/save")
    public void save(@RequestBody AnalyticsEvent event) {
        analyticsEventService.saveEvent(event);
    }

    @PostMapping("/get")
    public List<AnalyticsEventDto> getAnalyticsEvent(@Validated @RequestBody AnalyticsFilterDto analyticsFilterDto) {
        return analyticsEventService.getAnalytics(analyticsFilterDto);
    }

}