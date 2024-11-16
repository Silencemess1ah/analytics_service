package faang.school.analytics.scheduler;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.analytic.AnalyticsEventRepository;
import faang.school.analytics.service.analytic.AverageValueOfActionCalculator;
import faang.school.analytics.service.analytic.StandardDeviationCalculator;
import faang.school.analytics.service.user.UserRankUpdaterService;
import faang.school.analytics.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledRatingUpdater {
    private final static double RATING_GROWTH_INTENSIVE = 0.05;

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AverageValueOfActionCalculator averageValueOfActionCalculator;
    private final StandardDeviationCalculator standardDeviationCalculator;
    private final UserRankUpdaterService userRankUpdaterService;
    private final UserService userService;
    private final Map<String, HashMap<Long, Double>> usersActionsByEventType = new HashMap<>();
    private final Map<String, List<Integer>> sumOfUserActionsByEventType = new HashMap<>();
    private final Map<Long, Double> usersNewRanksByLastThreeHourActions = new HashMap<>();
    private Double averageValueOfAction = 0.0;
    private Double standardDeviationOfAction = 0.0;

    @Async
    @Scheduled(cron = "0 * * * * *", zone = "Europe/Moscow")
    public void updateUserRankScore() {
        log.info("updating users rank is starting");
        for (EventType eventType : EventType.values()) {
            String currentEventType = eventType.name();
            log.info(currentEventType);
            usersActionsByEventType.put(currentEventType,
                    mapToUsersActionsByEventType(
                            analyticsEventRepository
                                    .findUserActionCountByEventTypeAndTimeLimit(eventType.name())
                                    .orElseGet(ArrayList::new)));

            sumOfUserActionsByEventType.put(eventType.name(),
                    usersActionsByEventType.get(currentEventType).values().stream()
                            .map(Double::intValue)
                            .toList());
            log.info(sumOfUserActionsByEventType.toString());

            int allUsersSize = userService.getUsersSize();
            averageValueOfAction = averageValueOfActionCalculator
                    .calculate(sumOfUserActionsByEventType.get(currentEventType), allUsersSize);
            log.info(String.valueOf(averageValueOfAction));

            standardDeviationOfAction = standardDeviationCalculator
                    .calculate(sumOfUserActionsByEventType.get(currentEventType),
                            averageValueOfAction, allUsersSize);
            log.info(String.valueOf(standardDeviationOfAction));

            usersActionsByEventType.get(currentEventType).forEach((key, value) -> {
                if (usersNewRanksByLastThreeHourActions.containsKey(key)) {
                    usersNewRanksByLastThreeHourActions.merge(
                            key,
                            calculateZScore(averageValueOfAction, standardDeviationOfAction, value)
                                    * RATING_GROWTH_INTENSIVE * EventType.getWeightByName(currentEventType),
                            Double::sum);
                    log.info("value:" + value);
                    log.info("weight:" + EventType.getWeightByName(currentEventType));
                    log.info("intensive:" + RATING_GROWTH_INTENSIVE);
                    log.info("rating:" + usersNewRanksByLastThreeHourActions.get(key).toString());
                } else {
                    usersNewRanksByLastThreeHourActions.put(key,
                            calculateZScore(averageValueOfAction,
                                    standardDeviationOfAction,
                                    value) * RATING_GROWTH_INTENSIVE * EventType.getWeightByName(currentEventType));
                    log.info("value:" + value);
                    log.info("weight:" + EventType.getWeightByName(currentEventType));
                    log.info("intensive:" + RATING_GROWTH_INTENSIVE);
                    log.info("rating:" + usersNewRanksByLastThreeHourActions.get(key).toString());
                }
            });
        }
        userRankUpdaterService.updateUsersRankInBatch(usersNewRanksByLastThreeHourActions);
    }

    public HashMap<Long, Double> mapToUsersActionsByEventType(List<AnalyticsEvent> analyticsEvents) {
        return analyticsEvents.stream()
                .collect(Collectors.groupingBy(
                        AnalyticsEvent::getActorId,
                        HashMap::new,
                        Collectors.summingDouble(event -> 1.0)
                ));
    }

    public double calculateZScore(Double averageValueOfAction, Double standardDeviationOfAction, Double userActionsCount) {
        return (userActionsCount - averageValueOfAction) / standardDeviationOfAction;
    }
}
