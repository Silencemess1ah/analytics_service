package faang.school.analytics.config.redis;

import faang.school.analytics.listener.PostViewEventListener;
import faang.school.analytics.listener.CommentEventListener;
import faang.school.analytics.publish.listener.like.PostLikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
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
@RequiredArgsConstructor
@EnableCaching
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // adjust serializer if needed
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer messageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter postViewMessageListenerAdapter,
            MessageListenerAdapter postLikeMessageListenerAdapter,
            MessageListenerAdapter commentMessageListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(postViewMessageListenerAdapter, postViewEventTopic());
        container.addMessageListener(postLikeMessageListenerAdapter, postLikeEventTopic());
        container.addMessageListener(commentMessageListenerAdapter, commentEventTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter postViewMessageListenerAdapter(PostViewEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter postLikeMessageListenerAdapter(PostLikeEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public MessageListenerAdapter commentMessageListenerAdapter(CommentEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    public ChannelTopic postViewEventTopic() {
        return new ChannelTopic(redisProperties.getPostViewChannelName());
    }

    @Bean
    public ChannelTopic postLikeEventTopic() {
        return new ChannelTopic(redisProperties.getPostLikeEventChannelName());
    }

    @Bean
    public ChannelTopic commentEventTopic() {
        return new ChannelTopic(redisProperties.getCommentEventChannelName());
    }
}