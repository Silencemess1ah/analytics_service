package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(analyticsEventDto);
        when(analyticsEventRepository.save(analyticsEvent))
                .thenReturn(analyticsEvent);

        ResponseEntity<Void> response = analyticsEventService.saveAction(analyticsEventDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    public void saveAction_WithWrongDto_ThrowIllegalArgumentException() {
         assertThrows(IllegalArgumentException.class,
                 () -> analyticsEventMapper.toEntity(analyticsEventDtoWithWrongEventTypeNumber));
    }
}
