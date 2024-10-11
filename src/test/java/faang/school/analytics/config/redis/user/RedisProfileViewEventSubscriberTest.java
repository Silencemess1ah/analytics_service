package faang.school.analytics.config.redis.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.ProfileViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.user.listener.RedisProfileViewEventSubscriber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static faang.school.analytics.util.AnalyticFabric.buildAnalyticsEvents;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisProfileViewEventSubscriberTest {
    private static final String JSON = "{\"receiverId\":1,\"actorId\":2,\"receivedAt\":\"2024-10-08T18:46:50\"}";
    private static final long RECEIVER_ID = 1L;
    private static final long ACTOR_ID = 2L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 1, 1, 1);

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Mock
    private AnalyticsEventService analyticsEventService;

    @InjectMocks
    private RedisProfileViewEventSubscriber redisProfileViewEventSubscriber;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Received message of redis topic successful")
    void testOnMessage() throws IOException {
        Message message = mock(Message.class);
        ProfileViewEventDto profileViewEventDto = new ProfileViewEventDto(RECEIVER_ID, ACTOR_ID, LOCAL_DATE_TIME);
        byte[] bytes = JSON.getBytes(StandardCharsets.UTF_8);

        when(message.getBody()).thenReturn(bytes);
        when(objectMapper.readValue(bytes, ProfileViewEventDto.class)).thenReturn(profileViewEventDto);

        redisProfileViewEventSubscriber.onMessage(message, "test".getBytes());

        List<AnalyticsEvent> analyticsEvents = (List<AnalyticsEvent>)
                ReflectionTestUtils.getField(redisProfileViewEventSubscriber, "analyticsEvents");
        assertThat(analyticsEvents).isNotEmpty();
    }

    @Test
    @DisplayName("Test analytic events list is empty successful")
    void testAnalyticsEventsListIsEmpty() {
        assertThat(redisProfileViewEventSubscriber.analyticsEventsListIsEmpty()).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Given an exception when save list then catch and return copyList into main list")
    void testSaveAllUserViewEventsException() {
        List<AnalyticsEvent> events = new ArrayList<>(buildAnalyticsEvents(1L, 3L));
        ReflectionTestUtils.setField(redisProfileViewEventSubscriber, "analyticsEvents", events);
        when(analyticsEventService.saveAllEvents(anyList())).thenThrow(new RuntimeException());

        redisProfileViewEventSubscriber.saveAllUserViewEvents();

        List<AnalyticsEvent> eventsListFromTestObject = (List<AnalyticsEvent>)
                ReflectionTestUtils.getField(redisProfileViewEventSubscriber, "analyticsEvents");
        assertThat(eventsListFromTestObject).isNotEmpty();
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Save all user view events successful")
    void testSaveAllUserViewEventsSuccessful() {
        List<AnalyticsEvent> events = new ArrayList<>(buildAnalyticsEvents(1L, 3L));
        ReflectionTestUtils.setField(redisProfileViewEventSubscriber, "analyticsEvents", events);

        redisProfileViewEventSubscriber.saveAllUserViewEvents();

        List<AnalyticsEvent> eventsListFromTestObject = (List<AnalyticsEvent>)
                ReflectionTestUtils.getField(redisProfileViewEventSubscriber, "analyticsEvents");
        assertThat(eventsListFromTestObject).isEmpty();
    }
}