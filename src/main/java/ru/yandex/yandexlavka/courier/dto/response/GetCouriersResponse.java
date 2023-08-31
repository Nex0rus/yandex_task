package ru.yandex.yandexlavka.courier.dto.response;

import lombok.Builder;
import ru.yandex.yandexlavka.courier.dto.CourierDto;

import java.util.List;

@Builder
public record GetCouriersResponse(List<CourierDto> couriers, int limit, int offset) {
}
