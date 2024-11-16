package faang.school.analytics.service.analytic;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandardDeviationCalculator {

    public double calculate(List<Integer> sumOfUsersActions, double averageValueOfAction, int allUsersSize) {
        double standardDeviation = sumOfUsersActions.stream()
                .mapToDouble(num -> Math.pow(num - averageValueOfAction, 2))
                .sum() / allUsersSize;
        return Math.sqrt(standardDeviation);
    }
}
