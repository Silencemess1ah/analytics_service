package faang.school.analytics.messaging.premium;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.premium.PremiumBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class PremiumBoughtListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message {}", message);
        try {
            PremiumBoughtEvent premiumBoughtEvent = objectMapper.readValue(message.getBody(), PremiumBoughtEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.premiumBoughtToAnalytics(premiumBoughtEvent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
