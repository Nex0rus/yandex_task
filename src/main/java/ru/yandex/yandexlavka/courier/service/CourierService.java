package ru.yandex.yandexlavka.courier.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.yandexlavka.courier.dto.*;
import ru.yandex.yandexlavka.courier.dto.request.CreateCourierRequest;

import java.time.LocalDate;
import java.util.List;

public interface CourierService {
    List<CourierDto> createCouriers(CreateCourierRequest createCourierRequest);
    List<CourierDto> getAllCouriers(Pageable pageRequest);
    CourierDto getCourierById(long courierId);
    CourierMetaInfo getCourierMetaInfo(long courierId, LocalDate startDate, LocalDate endDate);
    List<CouriersGroupOrders> getOrderAssignments(LocalDate date, Long courierId);

}
