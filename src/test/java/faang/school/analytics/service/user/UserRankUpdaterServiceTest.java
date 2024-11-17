package faang.school.analytics.service.user;

import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRankUpdaterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserRankUpdaterService userRankUpdaterService;

    @Value("{rating-growth-intensive}")
    private double ratingGrowthIntensive;

    @Test
    void testUpdateUsersRankInBatch_WhenUsersHaveValidRanks() {
        Map<Long, Double> usersNewRanks = Map.of(
                1L, 10.555,
                2L, 20.123,
                3L, 0.0);

        userRankUpdaterService.updateUsersRankInBatch(usersNewRanks);

        verify(userRepository).updateUserRankByUserId(1L, 10.56);
        verify(userRepository).updateUserRankByUserId(2L, 20.12);
        verify(userRepository, times(0)).updateUserRankByUserId(3L, 0.0);
        verify(entityManager, times(1)).flush();
        verify(entityManager, times(1)).clear();
    }

    @Test
    void testUpdatePassiveUsers() {
        Map<Long, Double> usersNewRanks = Map.of(1L, 10.0, 2L, 15.0);
        Set<Long> activeUserIds = usersNewRanks.keySet();
        double maxPossibleRating = EventType.getMaximumRating() * ratingGrowthIntensive;

        userRankUpdaterService.updateUsersRankInBatch(usersNewRanks);

        verify(userRepository).updatePassiveUsersRatingWhichRatingLessThanRating(eq(maxPossibleRating), eq(activeUserIds));
        verify(userRepository).updatePassiveUsersRatingWhichRatingMoreThanRating(eq(maxPossibleRating), eq(activeUserIds));
    }

    @Test
    void testFlushAndClearCalled_WhenBatchLimitExceeded() {
        Map<Long, Double> usersNewRanks = new HashMap<>();
        for (long i = 1; i <= 60; i++) {
            usersNewRanks.put(i, (double) i);
        }

        userRankUpdaterService.updateUsersRankInBatch(usersNewRanks);

        verify(entityManager, times(2)).flush();
        verify(entityManager, times(2)).clear();
    }
}

