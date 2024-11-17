package faang.school.analytics.service.user;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public int getUsersSize() {
        return userRepository.getUsersSize()
                .orElse(0);
    }

    public HashMap<Long, Integer> mapToUsersActionsByEventType(List<AnalyticsEvent> analyticsEvents) {
        return analyticsEvents.stream()
                .collect(Collectors.groupingBy(
                        AnalyticsEvent::getActorId,
                        HashMap::new,
                        Collectors.summingInt(event -> 1)
                ));
    }

    public int getSumOfUsersActionsByEventType(List<Integer> usersActionsSumByEventType) {
        return usersActionsSumByEventType.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
