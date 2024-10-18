package faang.school.analytics.config.context.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class RedisTopicsFactory {
    @Value("${spring.data.redis.channel.mentorship_request_received}")
    private String mentorshipRequestReceivedTopicName;

    @Value("${spring.data.redis.channel.post-view}")
    private String postViewEventTopic;

    @Bean
    public ChannelTopic mentorshipRequestReceivedTopicName() {
        return new ChannelTopic(mentorshipRequestReceivedTopicName);
    }

    @Bean
    public ChannelTopic postViewEventTopic() {
        return new ChannelTopic(postViewEventTopic);
    }
}
