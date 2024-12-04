package faang.school.analytics.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Slf4j
public class PremiumBoughtListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message {}", message);
    }
}
