package faang.school.analytics.config.context.redis.listener_containers;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestContainer implements RedisContainerMessageListener {
    private final MessageListenerAdapter adapter;
    private final Topic topic;

    public MentorshipRequestContainer(MessageListener mentorshipRequestReceivedEventListener,
                                      Topic mentorshipRequestReceivedTopicName) {
        this.adapter = new MessageListenerAdapter(mentorshipRequestReceivedEventListener);
        this.topic = mentorshipRequestReceivedTopicName;
    }

    @Override
    public MessageListenerAdapter getAdapter() {
        return adapter;
    }

    @Override
    public Topic getTopic() {
        return topic;
    }
}
