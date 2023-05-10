package ru.yandex.yandexlavka.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CompleteOrderDto(@NotNull(message = "courier id must be not null")
                               @Min(value = 1, message = "courier id must be positive integer")
                               @JsonProperty("courier_id")
                               Long courierId,
                               @NotNull(message = "order id must be not null")
                               @Min(value = 1, message = "order id must be positive integer")
                               @JsonProperty("order_id")
                               Long orderId,
                               @NotNull(message = "complete time must be non null")
                               @JsonProperty("complete_time")
                               LocalDate completeTime) { }
