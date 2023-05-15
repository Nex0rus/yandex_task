package ru.yandex.yandexlavka.courier.components.type;

public interface CourierTypeFactory {
    CourierType getTypeOf(CourierTypeEnum courierTypeEnum);
}
