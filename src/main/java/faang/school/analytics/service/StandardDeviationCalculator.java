package faang.school.analytics.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandardDeviationCalculator {

    public double calculate(List<Integer> sumOfUsersActions, double averageValueOfAction) {
        double standardDeviation = sumOfUsersActions.stream()
                .mapToDouble(num -> Math.pow(num - averageValueOfAction, 2))
                .sum() / sumOfUsersActions.size();
        return Math.sqrt(standardDeviation);
    }
}
