package faang.school.analytics.service.analytic;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.entity.User;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import faang.school.analytics.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @Mock
    private UserService userService;

    private AnalyticsEventDto analyticsEventDto;
    private final AnalyticsEventDto analyticsEventDtoWithWrongEventTypeNumber =
            AnalyticsEventDto.builder()
                    .eventTypeNumber(-1)
                    .build();

    @BeforeEach
    void setUp() {
        analyticsEventDto = AnalyticsEventDto.builder()
                .actorId(1L)
                .receiverId(2L)
                .eventTypeNumber(EventType.FOLLOWER.ordinal())
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void saveAction_WithCorrectDto_ReturnOK() {
        ArgumentCaptor<AnalyticsEvent> analyticsEventArgumentCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticsEventDto);
        when(analyticsEventRepository.save(analyticsEventArgumentCaptor.capture()))
                .thenReturn(analyticsEvent);
        when(userService.findById(2L))
                .thenReturn(User.builder().build());

        ResponseEntity<Void> response = analyticsEventService.saveAction(analyticsEventDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(analyticsEventRepository, times(1)).save(analyticsEventArgumentCaptor.capture());
    }

    @Test
    public void saveAction_WithWrongDto_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> analyticsEventMapper.toEntity(analyticsEventDtoWithWrongEventTypeNumber));
    }
}
