package ru.yandex.yandexlavka.courier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yandex.yandexlavka.order.components.group.OrderGroup;
import ru.yandex.yandexlavka.order.dto.GroupOrderDto;

import java.util.List;

public record CouriersGroupOrders(@JsonProperty("courier_id") Long courierId, @JsonProperty("orders") List<GroupOrderDto> orderGroupList) {
}
