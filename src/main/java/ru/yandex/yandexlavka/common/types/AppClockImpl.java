package ru.yandex.yandexlavka.common.types;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class AppClockImpl implements AppClock {
    @Override
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
