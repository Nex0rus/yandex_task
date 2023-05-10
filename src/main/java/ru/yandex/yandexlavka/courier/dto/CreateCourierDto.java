package ru.yandex.yandexlavka.courier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import ru.yandex.yandexlavka.common.types.TimeInterval;
import ru.yandex.yandexlavka.courier.components.CourierTypeEnum;

import java.util.List;

public record CreateCourierDto(@NotNull(message = "courier type must be non null")
                               @JsonProperty("courier_type")
                               CourierTypeEnum type,
                               @NotNull(message = "array of regions must be non null")
                               @NotEmpty(message = "array of regions must contain at least one region")
                               List<@Min(value = 1, message = "region must be a positive integer") Integer> regions,
                               @NotNull(message = "array of working hours must be non null")
                               @NotEmpty(message = "array of working hours must contain at least one time interval")
                               @JsonProperty("working_hours")
                               List<TimeInterval> workingHours) {}
