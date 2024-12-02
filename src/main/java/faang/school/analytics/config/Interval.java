package faang.school.analytics.config;
import java.time.LocalDateTime;

public interface Interval {
    boolean contains(LocalDateTime dateTime);
}