package ru.yandex.yandexlavka.common.exceptions;

public class TimeException extends DomainException {
    private TimeException(String message) {
        super(message);
    }

    public static TimeException wrongFormatException(String timeInput, String format) {
        return new TimeException("Failed to parse time input : " + timeInput + " . Input time should match the format : " + format);
    }

    public static TimeException intervalWrongFormatException(String intervalInput, String format) {
        return new TimeException("Failed to parse time interval from input : " + intervalInput + " . Interval should match the format : " + format);
    }
}
