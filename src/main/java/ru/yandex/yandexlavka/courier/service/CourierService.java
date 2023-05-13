package ru.yandex.yandexlavka.courier.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.yandexlavka.courier.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface CourierService {
    CreateCouriersResponse createCouriers(CreateCourierRequest createCourierRequest);
    GetCouriersResponse getAllCouriers(Pageable pageRequest);
    CourierDto getCourierById(long courierId);
    CourierMetaInfoDto getCourierMetaInfo(long courierId, LocalDate startDate, LocalDate endDate);
}
