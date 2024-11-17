package faang.school.analytics.service.user;

import faang.school.analytics.entity.User;
import faang.school.analytics.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public int getSumOfUsersActionsByEventType(List<Integer> usersActionsSumByEventType) {
        return usersActionsSumByEventType.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found!"));
    }
}
