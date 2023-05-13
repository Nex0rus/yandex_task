package ru.yandex.yandexlavka.common.types;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AppClock {
    public LocalDateTime getCurrentTime();
}
