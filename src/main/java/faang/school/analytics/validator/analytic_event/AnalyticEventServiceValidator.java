package faang.school.analytics.validator.analytic_event;

import faang.school.analytics.model.AnalyticsEvent;
import org.springframework.stereotype.Component;

@Component
public class AnalyticEventServiceValidator {
    public void checkEntity(AnalyticsEvent analyticsEvent) {
        if (analyticsEvent.getEventType() == null || analyticsEvent.getActorId() < 0 || analyticsEvent.getReceiverId() < 0) {
            throw new IllegalArgumentException("Не валидные данные ");
        }
    }

    public void validateInterval(String interval, String from, String to) {
        if ((interval == null || interval.isBlank()) && ((from == null || from.isBlank()) || (to == null || from.isBlank()))) {
            throw new IllegalArgumentException("Интервал и временной промежуток не может быть равен нулю");
        }
    }

    public void checkIdAndEvent(long id, String event) {
        if (id < 0 || event == null || event.isBlank()) {
            throw new IllegalArgumentException("не валидный id или эвент");
        }
    }
}
