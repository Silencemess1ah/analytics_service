package faang.school.analytics.service;

import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;

    public void saveMentorshipRequestEvent(MentorshipRequestEvent mentorshipRequestEvent) {
        log.info("Saving MentorshipRequestEvent for receiverId={} and actorId={}",
                mentorshipRequestEvent.getReceiverId(), mentorshipRequestEvent.getActorId());

        AnalyticsEvent analyticsEvent = (AnalyticsEvent) analyticsEventMapper.toAnalyticsEventMentorshipRequest(mentorshipRequestEvent);
        analyticsEventRepository.save(analyticsEvent);

        log.info("Successfully saved AnalyticsEvent with receiverId={} and actorId={}",
                analyticsEvent.getReceiverId(), analyticsEvent.getActorId());
    }
}
