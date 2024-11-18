package faang.school.analytics.service.analytic;

import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRankUpdaterService {
    @Value("${rating-growth-intensive}")
    private double ratingGrowthIntensive;
    private final static int BATCH_SIZE = 50;
    private final UserRepository userRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public void updateUsersRankInBatch(Map<Long, Double> usersNewRanksByLastThreeHourActions) {
        log.info("batch is starting {}", usersNewRanksByLastThreeHourActions);
        int batchCounter = 1;
        for (Map.Entry<Long, Double> userNewRank : usersNewRanksByLastThreeHourActions.entrySet()) {
            if (userNewRank.getValue() != 0.0) {
                try {
                    BigDecimal value = BigDecimal.valueOf(userNewRank.getValue());
                    BigDecimal roundedValue = value.setScale(2, RoundingMode.HALF_UP);
                    userRepository.updateUserRankByUserId(userNewRank.getKey(), roundedValue.doubleValue());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    userRepository.updateUserRankByUserId(userNewRank.getKey(), BigDecimal.valueOf(0).doubleValue());
                }
                batchCounter++;
            }
            if (batchCounter % BATCH_SIZE == 0) {
                flushAndClear();
            }
        }
        BigDecimal maxPossibleRating = BigDecimal.valueOf(EventType.getMaximumRating() * ratingGrowthIntensive);
        BigDecimal roundedValue = maxPossibleRating.setScale(2, RoundingMode.HALF_UP);
        Set<Long> activeUsersIds = usersNewRanksByLastThreeHourActions.keySet();
        userRepository.updatePassiveUsersRatingWhichRatingLessThanRating(roundedValue.doubleValue(), activeUsersIds);
        userRepository.updatePassiveUsersRatingWhichRatingMoreThanRating(roundedValue.doubleValue(), activeUsersIds);
        flushAndClear();
    }

    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
