package ru.yandex.yandexlavka.courier.dto.request;

import ru.yandex.yandexlavka.courier.dto.CourierDto;

import java.util.List;

public record CreateCouriersResponse(List<CourierDto> couriers) {
}
