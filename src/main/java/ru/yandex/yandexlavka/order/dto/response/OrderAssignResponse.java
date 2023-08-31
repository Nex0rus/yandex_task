package ru.yandex.yandexlavka.order.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yandex.yandexlavka.courier.dto.CouriersGroupOrders;

import java.time.LocalDate;
import java.util.List;

public record OrderAssignResponse(LocalDate date, @JsonProperty("couriers") List<CouriersGroupOrders> couriersGroupOrdersList) {
}
