package faang.school.analytics.config.redis;

import faang.school.analytics.listener.ProjectViewEventListener;
import faang.school.analytics.listener.RecommendationEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties properties;

    @Bean
    public MessageListenerAdapter messageProjectViewListener(ProjectViewEventListener projectViewEventListener) {
        return new MessageListenerAdapter(projectViewEventListener);
    }

    @Bean
    public MessageListenerAdapter messageRecommendationListener(
            RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    public ChannelTopic projectViewEventTopic() {
        return new ChannelTopic(properties.getChannels().get("project-profile-attendance"));
    }

    @Bean
    public ChannelTopic recommendationEventTopic() {
        return new ChannelTopic(properties.getChannels().get("recommendation-event-channel"));
    }

    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messageProjectViewListener,
            MessageListenerAdapter messageRecommendationListener,
            ChannelTopic projectViewEventTopic,
            ChannelTopic recommendationEventTopic) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(messageProjectViewListener, projectViewEventTopic);
        container.addMessageListener(messageRecommendationListener, recommendationEventTopic);

        return container;
    }
}
