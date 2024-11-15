package faang.school.analytics.scheduler;

import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AverageValueOfActionCalculator;
import faang.school.analytics.service.StandardDeviationCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledRatingUpdater {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AverageValueOfActionCalculator averageValueOfActionCalculator;
    private final StandardDeviationCalculator standardDeviationCalculator;
    private final Map<String, HashMap<Long, Double>> usersActionsByEventType = new HashMap<>();
    private final HashMap<String, Double> averageValuesOfActionsByEventType = new HashMap<>();
    private final HashMap<String, Double> standardDeviationsByEventType = new HashMap<>();
    private final HashMap<String, List<Integer>> sumOfUserActionsByEventType = new HashMap<>();
    private final HashMap<Long, BigDecimal> usersNewRanksByLastThreeHourActions = new HashMap<>();

    @Async
    @Scheduled(cron = "0 0 */3 * * *", zone = "Europe/Moscow")
    public void updateUserRankScore() {
        for (EventType eventType : EventType.values()) {
            String currentEventType = eventType.name();
            usersActionsByEventType
                    .put(currentEventType,
                    analyticsEventRepository
                    .findUserActionCountByEventTypeAndTimeLimit(eventType, LocalDateTime.now().minusHours(3)));

            sumOfUserActionsByEventType.put(eventType.name(), usersActionsByEventType.get(currentEventType)
                    .values().stream()
                    .map(Double::intValue)
                    .toList());

            averageValuesOfActionsByEventType.put(currentEventType,
                    averageValueOfActionCalculator.calculate(sumOfUserActionsByEventType.get(currentEventType)));
            standardDeviationsByEventType.put(currentEventType,
                    standardDeviationCalculator.calculate(sumOfUserActionsByEventType.get(currentEventType),
                            averageValuesOfActionsByEventType.get(currentEventType)));

            usersActionsByEventType.get(currentEventType).entrySet().stream()
                    .peek(userActionSum -> {
                        if(usersNewRanksByLastThreeHourActions.get(userActionSum.getKey()) != null) {
                            usersNewRanksByLastThreeHourActions.put(userActionSum.getKey(), BigDecimal.valueOf(userActionSum.getValue()));
                        } else {
                            usersNewRanksByLastThreeHourActions.get(userActionSum.getKey()).add(BigDecimal.valueOf(userActionSum.getValue()));
                        }
                    });
        }

        for(Map.Entry<Long, BigDecimal> userNewRank : usersNewRanksByLastThreeHourActions.entrySet()) {
            if(userNewRank.getValue().intValue() != 0) {
                analyticsEventRepository.updateUserRankByUserId(userNewRank.getKey(), userNewRank.getValue());
            }
        }
    }
}
