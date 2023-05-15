package ru.yandex.yandexlavka.courier.components;

import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import ru.yandex.yandexlavka.courier.dto.CourierDto;
import ru.yandex.yandexlavka.courier.dto.CouriersGroupOrders;
import ru.yandex.yandexlavka.courier.model.Courier;
import ru.yandex.yandexlavka.order.components.group.OrderGroupExtensions;

import java.util.List;

@UtilityClass
@ExtensionMethod(OrderGroupExtensions.class)
public class CourierExtensions {
    public static CourierDto toDto(Courier courier) {
       return new CourierDto(courier.getId(), courier.getType().getTypeEnum(), courier.getRegions(), courier.getWorkingHours());
    }

    public static CouriersGroupOrders toCourierGroupOrdersDto(Courier courier) {
        return new CouriersGroupOrders(courier.getId(), courier.getOrderGroup() == null ? List.of() : List.of(courier.getOrderGroup().toDto()));
    }
}
