package faang.school.analytics.repository.user;

import faang.school.analytics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query("""
    UPDATE User u SET u.rankScore = u.rankScore + :rank WHERE u.id = :userId
    """)
    void updateUserRankByUserId(@Param("userId") Long userId, @Param("rank") double rank);

    @Query("""
    SELECT COUNT(*) FROM User u
    """)
    Optional<Integer> getUsersSize();

    @Modifying
    @Transactional
    @Query("""
    UPDATE User u SET u.rankScore = u.rankScore - :rank WHERE u.id NOT IN(:excludedIds)
    """)
    void updateDontActiveUsersRank(@Param("rank") double rank, @Param("excludedIds") List<Integer> excludedIds);
}
