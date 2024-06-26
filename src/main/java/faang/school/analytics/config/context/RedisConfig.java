package faang.school.analytics.config.context;

import faang.school.analytics.listener.CommentEventEventListener;
import faang.school.analytics.listener.LikeEventListener;
import faang.school.analytics.listner.PostViewEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.channel.post-view-channel}")
    private String postViewChannelName;

    @Value("${spring.data.redis.channel.comment-event-chanel}")
    private String commentEventChannelName;
    @Value("${spring.data.redis.channel.like-view}")
    private String likeChannelName;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public MessageListenerAdapter likeEventAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(likeChannelName);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    MessageListenerAdapter commentEventListener(CommentEventEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean()
    public ChannelTopic commentEventTopic() {
        return new ChannelTopic(commentEventChannelName);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter postViewListener,
                                                                       MessageListenerAdapter commentEventListener,
                                                                       MessageListenerAdapter likeEventAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        container.addMessageListener(postViewListener, postViewTopic());
        container.addMessageListener(commentEventListener, commentEventTopic());
        container.addMessageListener(likeEventAdapter, likeTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter postViewListener(PostViewEventListener postViewEventListener) {
        return new MessageListenerAdapter(postViewEventListener);
    }

    @Bean
    public ChannelTopic postViewTopic() {
        return new ChannelTopic(postViewChannelName);
    }
}
