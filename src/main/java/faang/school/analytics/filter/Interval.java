package faang.school.analytics.filter;
import java.time.LocalDateTime;

public interface Interval {
    boolean contains(LocalDateTime dateTime);
}