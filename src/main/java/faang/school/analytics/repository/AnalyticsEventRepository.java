package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {

    Stream<AnalyticsEvent> findByReceiverIdAndEventType(long receiverId, EventType eventType);

    @Query("""
        SELECT a.actorId, SUM(a.actorId) FROM AnalyticsEvent a
        WHERE a.eventType = :name AND a.receivedAt >= :timeLimit
        GROUP BY a.actorId
    """)
    HashMap<Long, Double> findUserActionCountByEventTypeAndTimeLimit(EventType name, LocalDateTime localDateTime);

    @Query("""
    UPDATE User u SET u.rankScore = u.rankScore + :rank WHERE u.id = :userId
    """)
    void updateUserRankByUserId(@Param("userId") Long userId, @Param("rank") BigDecimal rank);
}
