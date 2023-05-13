package ru.yandex.yandexlavka.order.components;

import lombok.experimental.UtilityClass;
import ru.yandex.yandexlavka.order.dto.OrderDto;
import ru.yandex.yandexlavka.order.model.Order;

import java.time.LocalDateTime;

@UtilityClass
public class OrderExtensions {
    public static OrderDto toDto(Order order, LocalDateTime completedTime) {
        return OrderDto.getInstanceWithCompletedTime(order.getId(),
                order.getCost(),
                order.getWeight(),
                order.getRegion(),
                order.getDeliveryHours(),
                completedTime);
    }
}
