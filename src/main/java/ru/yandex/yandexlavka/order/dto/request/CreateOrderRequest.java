package ru.yandex.yandexlavka.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(@NotNull(message = "array of orders must be not null") List<@Valid CreateOrderDto> orders) {
}
