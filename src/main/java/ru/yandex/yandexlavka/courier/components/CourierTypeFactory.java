package ru.yandex.yandexlavka.courier.components;

public interface CourierTypeFactory {
    CourierType getTypeOf(CourierTypeEnum courierTypeEnum);
}
