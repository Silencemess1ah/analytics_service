package faang.school.analytics.service.analytic;

import faang.school.analytics.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AverageValueOfActionCalculator {
    private final UserRepository userRepository;

    public double calculate(List<Integer> sumOfUsersActions, int allUsersSize) {
        return sumOfUsersActions.stream()
                .mapToDouble(Integer::intValue)
                .sum() / allUsersSize;
    }
}
