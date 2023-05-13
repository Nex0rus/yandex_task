package ru.yandex.yandexlavka.order.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.yandexlavka.order.dto.*;

import java.util.List;

public interface OrderService {
    List<OrderDto> createOrders(CreateOrderRequest createOrderRequest);

    List<OrderDto> getAllOrders(Pageable pageRequest);

    OrderDto getOrderById(long orderId);

    List<OrderDto> completeOrders(CompleteOrderRequest completeOrderRequest);
}
