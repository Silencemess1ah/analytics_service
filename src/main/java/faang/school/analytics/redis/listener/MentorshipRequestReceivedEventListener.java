package faang.school.analytics.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestReceivedDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MentorshipRequestReceivedEventListener extends AbstractEventListener<MentorshipRequestReceivedDto> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public MentorshipRequestReceivedEventListener(ObjectMapper objectMapper,
                                                  AnalyticsEventService analyticsEventService,
                                                  AnalyticsEventMapper analyticsEventMapper,
                                                  @Value("${spring.data.redis.channel.mentorship_request_received}")
                                                  String mentorshipRequestReceivedTopicName) {
        super(objectMapper, new ChannelTopic(mentorshipRequestReceivedTopicName));
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void saveEvent(MentorshipRequestReceivedDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.mentorshipRequestReceivedDtoToAnalyticsEvent(event);
        analyticsEventService.saveEvent(analyticsEvent);
        log.info("Event saved: {}", event);
    }

    @Override
    public Class<MentorshipRequestReceivedDto> getEventType() {
        return MentorshipRequestReceivedDto.class;
    }
}
