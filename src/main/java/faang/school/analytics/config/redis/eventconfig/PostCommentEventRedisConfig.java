package faang.school.analytics.config.redis.eventconfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class PostCommentEventRedisConfig extends AbstractEventRedisConfig {

    public PostCommentEventRedisConfig(
            @Value("${spring.data.redis.channels.comment-channel.name}") String topicName,
            @Qualifier("postCommentEventListener") MessageListener eventListener
    ) {
        super(topicName, eventListener);
    }

    @Override
    @Bean(name = "commentChannel")
    public ChannelTopic getTopic() {
        return this.topic;
    }

    @Override
    @Bean(name = "postCommentAdapter")
    public MessageListener getAdapter() {
        return this.adapter;
    }
}
