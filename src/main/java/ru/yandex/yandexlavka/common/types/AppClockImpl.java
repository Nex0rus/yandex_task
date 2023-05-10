package ru.yandex.yandexlavka.common.types;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
@Component
public class AppClockImpl implements AppClock {
    @Override
    public LocalDate getCurrentTime() {
        return LocalDate.now();
    }
}
