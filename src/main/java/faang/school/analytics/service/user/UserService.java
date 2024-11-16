package faang.school.analytics.service.user;

import faang.school.analytics.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public int getUsersSize() {
        return userRepository.getUsersSize()
                .orElseThrow(() -> new IllegalArgumentException("users size is null"));
    }
}
