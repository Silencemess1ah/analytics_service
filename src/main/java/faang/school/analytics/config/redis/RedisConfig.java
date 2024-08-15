package faang.school.analytics.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

import java.util.List;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration
                = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   List<Pair<MessageListenerAdapter, ChannelTopic>> listenerChannelPairs) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        listenerChannelPairs.forEach(pair -> container.addMessageListener(pair.getFirst(), pair.getSecond()));
        return container;
    }
}
