package ru.yandex.yandexlavka.order.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.yandexlavka.order.dto.CompleteOrderDto;
import ru.yandex.yandexlavka.order.dto.CreateOrderDto;
import ru.yandex.yandexlavka.order.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> createOrders(List<CreateOrderDto> createOrderRequests);

    List<OrderDto> getAllOrders(Pageable pageRequest);

    OrderDto getOrderById(long orderId);

    List<OrderDto> completeOrders(List<CompleteOrderDto> completeOrderRequests);
}
