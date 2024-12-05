package faang.school.analytics.service;

import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private MentorshipRequestEvent mentorshipRequestEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    void setUp() {
        mentorshipRequestEvent = new MentorshipRequestEvent(1L, 2L, null);
        analyticsEvent = new AnalyticsEvent();
    }

    @Test
    void saveMentorshipRequestEventSuccessfully() {
        when(analyticsEventMapper.toAnalyticsEventMentorshipRequest(mentorshipRequestEvent))
                .thenReturn(analyticsEvent);

        analyticsEventService.saveMentorshipRequestEvent(mentorshipRequestEvent);

        verify(analyticsEventMapper, times(1)).toAnalyticsEventMentorshipRequest(mentorshipRequestEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }


    @Test
    void saveMentorshipRequestEvent_ShouldThrowExceptionWhenRepositoryFails() {
        when(analyticsEventMapper.toAnalyticsEventMentorshipRequest(mentorshipRequestEvent))
                .thenReturn(analyticsEvent);
        doThrow(new RuntimeException("Database error")).when(analyticsEventRepository).save(analyticsEvent);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            analyticsEventService.saveMentorshipRequestEvent(mentorshipRequestEvent);
        });

        assertEquals("Database error", exception.getMessage());
    }
}