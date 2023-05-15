package ru.yandex.yandexlavka.courier.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.yandex.yandexlavka.courier.dto.request.CreateCourierDto;

import java.util.List;

public record CreateCourierRequest(@NotNull(message = "array of couriers must be not null")
                                   List<@Valid CreateCourierDto> couriers) {
}
