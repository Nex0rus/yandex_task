package ru.yandex.yandexlavka.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yandex.yandexlavka.order.model.Order;

import java.util.List;

public record GroupOrderDto(@JsonProperty("group_order_id") Long groupOrderId, List<OrderDto> orders) {
}
