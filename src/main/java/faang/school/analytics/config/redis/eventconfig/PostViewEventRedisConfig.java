package faang.school.analytics.config.redis.eventconfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class PostViewEventRedisConfig extends AbstractEventRedisConfig {

    public PostViewEventRedisConfig(
            @Value("${spring.data.redis.channels.post-view-channel.name}") String topicName,
            @Qualifier("postViewEventListener") MessageListener eventListener) {
        super(topicName, eventListener);
    }

    @Override
    @Bean("postViewChannel")
    public ChannelTopic getTopic() {
        return this.topic;
    }

    @Override
    @Bean("postViewAdapter")
    public MessageListener getAdapter() {
        return this.adapter;
    }
}
