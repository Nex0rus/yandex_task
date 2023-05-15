package ru.yandex.yandexlavka.order.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.yandexlavka.courier.dto.CouriersGroupOrders;
import ru.yandex.yandexlavka.order.dto.*;
import ru.yandex.yandexlavka.order.dto.request.CompleteOrderRequest;
import ru.yandex.yandexlavka.order.dto.request.CreateOrderRequest;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderDto> createOrders(CreateOrderRequest createOrderRequest);

    List<OrderDto> getAllOrders(Pageable pageRequest);

    OrderDto getOrderById(long orderId);

    List<OrderDto> completeOrders(CompleteOrderRequest completeOrderRequest);

    List<CouriersGroupOrders> assign(LocalDate assignDate);
}
