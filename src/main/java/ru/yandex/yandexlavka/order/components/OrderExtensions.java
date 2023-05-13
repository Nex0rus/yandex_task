package ru.yandex.yandexlavka.order.components;

import lombok.experimental.UtilityClass;
import org.aspectj.weaver.ast.Or;
import ru.yandex.yandexlavka.order.dto.OrderDto;
import ru.yandex.yandexlavka.order.model.Order;

import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class OrderExtensions {
    public static OrderDto toDto(Order order) {
        Optional<OrderStatusUpdate> completionUpdate = order.getLastUpdateWithStatus(OrderStatus.COMPLETED);
        return completionUpdate.map(orderStatusUpdate -> OrderDto.getInstanceWithCompletedTime(order.getId(),
                order.getCost(),
                order.getWeight(),
                order.getRegion(),
                order.getDeliveryHours(),
                orderStatusUpdate.getUpdateTime())).orElseGet(() -> OrderDto.getInstance(order.getId(),
                order.getCost(),
                order.getWeight(),
                order.getRegion(),
                order.getDeliveryHours()));
    }
}
