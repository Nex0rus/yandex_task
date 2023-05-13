package ru.yandex.yandexlavka.order.service;

import lombok.experimental.ExtensionMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.common.exceptions.OrderException;
import ru.yandex.yandexlavka.common.types.AppClock;
import ru.yandex.yandexlavka.courier.model.Courier;
import ru.yandex.yandexlavka.order.components.OrderExtensions;
import ru.yandex.yandexlavka.order.components.OrderStatusUpdateRepository;
import ru.yandex.yandexlavka.order.dto.*;
import ru.yandex.yandexlavka.order.model.Order;
import ru.yandex.yandexlavka.order.OrderRepository;
import ru.yandex.yandexlavka.order.components.OrderStatus;
import ru.yandex.yandexlavka.order.components.OrderStatusUpdate;

import java.util.*;
@Service
@ExtensionMethod(OrderExtensions.class)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderStatusUpdateRepository orderStatusUpdateRepository;

    private final AppClock clock;
    public OrderServiceImpl(OrderRepository orderRepository, OrderStatusUpdateRepository orderStatusUpdateRepository, AppClock clock) {
        this.orderRepository = orderRepository;
        this.orderStatusUpdateRepository = orderStatusUpdateRepository;
        this.clock = clock;
    }


    @Override
    public List<OrderDto> createOrders(CreateOrderRequest createOrderRequest) {
        List<Order> orders = createOrderRequest.orders().stream()
                .map(request ->
                {
                    Order newOrder = new Order(request.cost(), request.weight(), request.regions(), new HashSet<>(request.deliveryHours()));
                    orderRepository.save(newOrder);
                    OrderStatusUpdate newOrderUpdate = new OrderStatusUpdate(newOrder, OrderStatus.NEW, clock.getCurrentTime());
                    newOrder.getUpdateHistory().add(newOrderUpdate);
                    orderStatusUpdateRepository.save(newOrderUpdate);
                    return newOrder;
                }
                ).toList();
        return getOrderDtos(orders);
    }

    @Override
    public List<OrderDto> getAllOrders(Pageable pageRequest) {
        return getOrderDtos(orderRepository.findAll(pageRequest).getContent());
    }

    @Override
    public OrderDto getOrderById(long orderId) {
        return getOrder(orderId).toDto();
    }

    @Override
    public List<OrderDto> completeOrders(CompleteOrderRequest completeOrderRequest) {
        List<OrderDto> orderDtos = new ArrayList<>();
        completeOrderRequest.completeInfo().forEach(request -> {
            Order order = getOrder(request.orderId());
            validateCompleteRequestAndElseThrow(order, request);
            OrderStatusUpdate completionStatusUpdate = new OrderStatusUpdate(order, OrderStatus.COMPLETED, request.completeTime());
            order.getUpdateHistory().add(completionStatusUpdate);
            orderDtos.add(order.toDto());
        });

        return orderDtos;
    }

    private List<OrderDto> getOrderDtos(List<Order> orders) {
        return orders.stream().map(order -> order.toDto()).toList();
    }

    private void validateCompleteRequestAndElseThrow(Order order, CompleteOrderDto request) {
        Optional<OrderStatusUpdate> updateWithCompletion = order.getLastUpdateWithStatus(OrderStatus.COMPLETED);
        if (updateWithCompletion.isPresent()) {
            throw OrderException.orderAlreadyCompletedException(order.getId());
        }
        Courier courierAssignedTo = order.getCourier();
        if (courierAssignedTo == null) {
            throw OrderException.orderNotAssignedException(order.getId(), request.courierId());
        } else if (!courierAssignedTo.getId().equals(request.courierId())) {
            throw OrderException.orderAssignedToOtherCourierException(order.getId(), request.courierId());
        }
    }

    private Order getOrder(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> OrderException.orderNotFoundException(orderId));
    }
}
