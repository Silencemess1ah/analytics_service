package faang.school.analytics.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AverageValueOfActionCalculator {

    public double calculate(List<Integer> sumOfUsersActions) {
        return sumOfUsersActions.stream()
                .mapToDouble(Integer::intValue)
                .sum() / sumOfUsersActions.size();
    }
}
