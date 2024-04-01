package faang.school.analytics.config.redis;

import faang.school.analytics.listener.CommentEventListener;
import faang.school.analytics.listener.MentorshipRequestedEventListener;
import faang.school.analytics.listener.FollowerEventListener;
import faang.school.analytics.listener.RecommendationEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.channel.follower_channel}")
    private String followerEventTopic;
    @Value("${spring.data.redis.channel.recommendation}")
    private String recommendationChannelName;
    @Value("${spring.data.redis.channel.mentorship-requested}")
    private String mentorshipRequestedChannelName;
    @Value("${spring.data.redis.channel.comment}")
    private String commentChannelName;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    MessageListenerAdapter recommendationListener(RecommendationEventListener recommendationEventListener) {
        return new MessageListenerAdapter(recommendationEventListener);
    }

    @Bean
    ChannelTopic commentTopic() {
        return new ChannelTopic(commentChannelName);
    }

    @Bean
    MessageListenerAdapter commentListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    MessageListenerAdapter mentorshipRequestedListener(MentorshipRequestedEventListener mentorshipRequestedEventListener) {
        return new MessageListenerAdapter(mentorshipRequestedEventListener);
    }

    @Bean
    ChannelTopic recommendationTopic() {
        return new ChannelTopic(recommendationChannelName);
    }

    @Bean
    ChannelTopic mentorshipRequestedTopic() {
        return new ChannelTopic(mentorshipRequestedChannelName);
    }
    
    @Bean
    MessageListenerAdapter followerListener(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    ChannelTopic followerTopic() {
        return new ChannelTopic(followerEventTopic);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter recommendationListener,
                                                 MessageListenerAdapter followerListener,
                                                 MessageListenerAdapter mentorshipRequestedListener,
                                                 MessageListenerAdapter commentListener) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(recommendationListener, recommendationTopic());
        container.addMessageListener(mentorshipRequestedListener, mentorshipRequestedTopic());
        container.addMessageListener(followerListener, followerTopic());
        container.addMessageListener(commentListener, commentTopic());
        return container;
    }
}
