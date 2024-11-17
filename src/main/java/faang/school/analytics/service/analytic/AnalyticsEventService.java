package faang.school.analytics.service.analytic;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import faang.school.analytics.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final UserService userService;

    public ResponseEntity<Void> saveAction(AnalyticsEventDto analyticsEventDto) {
        userService.findById(analyticsEventDto.getReceiverId());
        analyticsEventDto.setReceivedAt(LocalDateTime.now());
        log.info("getting action: {}, from userId: {}", analyticsEventDto.toString(), analyticsEventDto.getReceiverId());
        analyticsEventRepository.save(analyticsEventMapper.toEntity(analyticsEventDto));
        log.info("success saved action of userId: {}", analyticsEventDto.getReceiverId());
        return ResponseEntity.ok().build();
    }

    public Map<Long, Integer> mapAnalyticEventsToActorActionsCount(List<AnalyticsEvent> analyticsEvents) {
        return analyticsEvents.stream()
                .collect(Collectors.groupingBy(
                        AnalyticsEvent::getActorId,
                        HashMap::new,
                        Collectors.summingInt(event -> 1)
                ));
    }
}
