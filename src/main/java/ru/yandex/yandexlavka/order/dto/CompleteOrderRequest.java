package ru.yandex.yandexlavka.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CompleteOrderRequest(@JsonProperty("complete_info")
                                   @NotNull(message = "array of requests for order completing must be not null")
                                   List<@Valid CompleteOrderDto> completeInfo) {
}
