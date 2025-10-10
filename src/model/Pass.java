package model;

import utils.Enums.ZoneType;
import utils.Enums.PassDuration;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Pass extends Product {

    private PassDuration duration;
    private LocalDateTime startDateTime;
    private LocalDateTime expiryDateTime;

    // Constructor
    public Pass(ZoneType zone, PassDuration duration, boolean printed) {
        super(zone, printed);
        this.duration = duration;
        this.startDateTime = LocalDateTime.now();
        this.expiryDateTime = calculateExpiry(startDateTime, duration);
    }

    // Getters
    public PassDuration getDuration() { return duration; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getExpiryDateTime() { return expiryDateTime; }

    private LocalDateTime calculateExpiry(LocalDateTime start, PassDuration duration) {
        return switch (duration) {
            case NIGHT -> start.toLocalDate().plusDays(1).atTime(LocalTime.of(6, 0));
            case DAY -> start.toLocalDate().atTime(LocalTime.of(23, 59, 59));
            case WEEKEND -> start.plusDays(2);
            case THREEDAY -> start.plusDays(3);
            case WEEK -> start.plusWeeks(1);
            case MONTH -> start.plusMonths(1);
            case FOURMONTH -> start.plusMonths(4);
        };
    }

    @Override
    public boolean isValid() {
        return valid && LocalDateTime.now().isBefore(expiryDateTime);
    }

    @Override
    public String toString() {
        return "Pass{" +
                "zone=" + zone +
                ", duration=" + duration +
                ", start=" + startDateTime +
                ", expiry=" + expiryDateTime +
                ", valid=" + isValid() +
                ", isPrinted=" + isPrinted +
                '}';
    }
}