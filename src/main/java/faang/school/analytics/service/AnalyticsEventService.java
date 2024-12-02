package faang.school.analytics.service;

import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsEventService {

    private final AnalyticsEventMapper mapper;

    public AnalyticsEventService(AnalyticsEventMapper mapper) {
        this.mapper = mapper;
    }

    public void processEvent(SearchAppearanceEvent event) {
        String logEntry = mapper.mapToLog(event);
        System.out.println("Processing event: " + logEntry);
    }
}