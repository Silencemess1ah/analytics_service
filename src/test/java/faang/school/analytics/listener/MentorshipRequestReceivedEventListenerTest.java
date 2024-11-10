package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestReceivedDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.listener.MentorshipRequestReceivedEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestReceivedEventListenerTest {

    @Mock
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private ObjectMapper objectMapper;

    private MentorshipRequestReceivedEventListener mentorshipRequestReceivedEventListener;

    @BeforeEach
    void setUp() {
        mentorshipRequestReceivedEventListener = new MentorshipRequestReceivedEventListener(
                objectMapper,
                analyticsEventService,
                analyticsEventMapper,
                "testViewChannel"
        );
    }

    @Test
    void testSaveEvent() {
        MentorshipRequestReceivedDto mentorshipRequestReceivedDto =
                new MentorshipRequestReceivedDto(1L, 2L, 3L, LocalDateTime.now());
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        when(analyticsEventMapper.mentorshipRequestReceivedDtoToAnalyticsEvent(mentorshipRequestReceivedDto))
                .thenReturn(analyticsEvent);

        mentorshipRequestReceivedEventListener.saveEvent(mentorshipRequestReceivedDto);

        verify(analyticsEventMapper, times(1))
                .mentorshipRequestReceivedDtoToAnalyticsEvent(mentorshipRequestReceivedDto);
        verify(analyticsEventService, times(1)).saveEvent(analyticsEvent);
    }
}