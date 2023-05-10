package ru.yandex.yandexlavka.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.yandex.yandexlavka.common.types.TimeInterval;

import java.util.List;

public record CreateOrderDto(@Min(value = 1, message = "cost must be a positive integer")
                             @NotNull(message = "cost must be not null")
                             Integer cost,
                             @NotNull(message = "weight must be not null")
                             Float weight,
                             @Min(value = 1, message = "region must be a positive integer")
                             @NotNull(message = "region must be not null")
                             Integer regions,
                             @NotNull(message = "array of delivery hours must be non null")
                             @NotEmpty(message = "array of delivery hours must contain at least one time interval")
                             @JsonProperty("delivery_hours")
                             List<TimeInterval> deliveryHours) { }
