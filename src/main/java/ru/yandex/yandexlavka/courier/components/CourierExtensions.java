package ru.yandex.yandexlavka.courier.components;

import lombok.experimental.UtilityClass;
import ru.yandex.yandexlavka.courier.dto.CourierDto;
import ru.yandex.yandexlavka.courier.model.Courier;

@UtilityClass
public class CourierExtensions {
    public static CourierDto toDto(Courier courier) {
       return new CourierDto(courier.getId(), courier.getType().getTypeEnum(), courier.getRegions(), courier.getWorkingHours());
    }
}
