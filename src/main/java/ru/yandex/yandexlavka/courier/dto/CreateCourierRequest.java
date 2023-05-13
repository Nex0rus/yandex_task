package ru.yandex.yandexlavka.courier.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateCourierRequest(@NotNull(message = "array of couriers must be not null")
                                   List<@Valid CreateCourierDto> couriers) {
}
