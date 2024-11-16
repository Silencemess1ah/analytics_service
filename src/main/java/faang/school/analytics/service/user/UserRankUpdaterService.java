package faang.school.analytics.service.user;

import faang.school.analytics.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRankUpdaterService {
    private final static int BATCH_SIZE = 50;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void updateUsersRankInBatch(Map<Long, Double> usersNewRanksByLastThreeHourActions) {
        log.info("batch is starting {}", usersNewRanksByLastThreeHourActions);
        int batchCounter = 0;
        for (Map.Entry<Long, Double> userNewRank : usersNewRanksByLastThreeHourActions.entrySet()) {
            if (userNewRank.getValue().doubleValue() != 0) {
                try{
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
        flushAndClear();
    }

    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
