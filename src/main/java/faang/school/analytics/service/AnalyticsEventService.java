package faang.school.analytics.service;

import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventMapper mapper;

    public void processEvent(SearchAppearanceEvent event) {
        String logEntry = mapper.mapToLog(event);
        log.info("Processing event: " + logEntry);
    }
}