package ru.yandex.yandexlavka.order.components.group;

import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import ru.yandex.yandexlavka.order.components.OrderExtensions;
import ru.yandex.yandexlavka.order.dto.GroupOrderDto;

@UtilityClass
@ExtensionMethod(OrderExtensions.class)
public class OrderGroupExtensions {
    public static GroupOrderDto toDto(OrderGroup orderGroup) {
        return new GroupOrderDto(orderGroup.getId(),orderGroup.getOrders().stream().map(order -> order.toDto()).toList());
    }
}
