package ru.yandex.yandexlavka.common.types;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.yandex.yandexlavka.common.exceptions.TimeException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Embeddable
@NoArgsConstructor
public class TimeInterval {
    @Transient
    private static final String timeFormat = "H:mm";
    @Transient
    private static final String separator = "-";
    @Transient
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeFormat);
    @Getter
    private LocalTime intervalStart;
    @Getter
    private LocalTime intervalEnd;

    public TimeInterval(String stringInterval) {
        String[] splitTime = stringInterval.split(separator);
        if (splitTime.length != 2) {
                throw TimeException.intervalWrongFormatException(stringInterval, timeFormat + separator + timeFormat);
        }
        try {
            this.intervalStart = LocalTime.parse(splitTime[0], dateTimeFormatter);
            this.intervalEnd = LocalTime.parse(splitTime[1], dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw TimeException.wrongFormatException(stringInterval, timeFormat);
        }

        if (intervalStart.isAfter(intervalEnd)) {
            throw TimeException.intervalWrongFormatException(stringInterval, timeFormat + separator + timeFormat);
        }
    }

    public String getStringInterval() {
        return intervalStart.format(dateTimeFormatter) + separator + intervalEnd.format(dateTimeFormatter);
    }

    public boolean belongsTo(TimeInterval other) {
        if (other == null) { return false; }
        return !intervalStart.isBefore(other.intervalStart) && !intervalEnd.isAfter(other.intervalEnd);
    }

    public String getIntervalFormat() {
        return timeFormat + separator + timeFormat;
    }
}
