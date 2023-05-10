package ru.yandex.yandexlavka.courier.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.yandexlavka.courier.dto.CourierDto;
import ru.yandex.yandexlavka.courier.dto.CourierMetaInfoDto;
import ru.yandex.yandexlavka.courier.dto.CreateCourierDto;

import java.time.LocalDate;
import java.util.List;

public interface CourierService {
    List<CourierDto> createCouriers(List<CreateCourierDto> createCourierRequests);
    List<CourierDto> getAllCouriers(Pageable pageRequest);
    CourierDto getCourierById(long courierId);
    CourierMetaInfoDto getCourierMetaInfo(long courierId, LocalDate startDate, LocalDate endDate);
}
