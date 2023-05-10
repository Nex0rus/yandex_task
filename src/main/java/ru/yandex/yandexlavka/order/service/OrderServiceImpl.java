package ru.yandex.yandexlavka.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.common.exceptions.OrderException;
import ru.yandex.yandexlavka.common.types.AppClock;
import ru.yandex.yandexlavka.courier.model.Courier;
import ru.yandex.yandexlavka.order.components.OrderStatusUpdateRepository;
import ru.yandex.yandexlavka.order.model.Order;
import ru.yandex.yandexlavka.order.OrderRepository;
import ru.yandex.yandexlavka.order.components.OrderStatus;
import ru.yandex.yandexlavka.order.components.OrderStatusUpdate;
import ru.yandex.yandexlavka.order.dto.CompleteOrderDto;
import ru.yandex.yandexlavka.order.dto.CreateOrderDto;
import ru.yandex.yandexlavka.order.dto.OrderDto;

import java.util.*;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderStatusUpdateRepository orderStatusUpdateRepository;
    @Autowired
    AppClock clock;
    @Override
    public List<OrderDto> createOrders(List<CreateOrderDto> createOrderRequests) {
        List<Order> orders = createOrderRequests.stream()
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
//        orderRepository.saveAll(orders);
        return getOrderDtos(orders);
    }

    @Override
    public List<OrderDto> getAllOrders(Pageable pageRequest) {
        return getOrderDtos(orderRepository.findAll(pageRequest).getContent());
    }

    @Override
    public OrderDto getOrderById(long orderId) {
        return toOrderDto(getOrder(orderId));
    }

    @Override
    public List<OrderDto> completeOrders(List<CompleteOrderDto> completeOrderRequests) {
        List<OrderDto> orderDtos = new ArrayList<>();
        completeOrderRequests.forEach(request -> {
            Order order = getOrder(request.orderId());
            validateCompleteRequestAndElseThrow(order, request);
            OrderStatusUpdate completionStatusUpdate = new OrderStatusUpdate(order, OrderStatus.COMPLETED, request.completeTime());
            order.getUpdateHistory().add(completionStatusUpdate);
            orderDtos.add(toOrderDto(order));
        });

        return orderDtos;
    }

    private List<OrderDto> getOrderDtos(List<Order> orders) {
        return orders.stream().map(this::toOrderDto).toList();
    }

    private OrderDto toOrderDto(Order order) {
        Optional<OrderStatusUpdate> updateWithCompletion = order.getLastUpdateWithStatus(OrderStatus.COMPLETED);
        return updateWithCompletion
                .map(orderStatusUpdate ->
                        OrderDto.getInstanceWithCompletedTime(order.getId(), order.getCost(), order.getWeight(), order.getRegion(), order.getDeliveryHours(), orderStatusUpdate.getUpdateTime()))
                .orElseGet(() ->
                        OrderDto.getInstance(order.getId(), order.getCost(), order.getWeight(), order.getRegion(), order.getDeliveryHours())
                );
    }

    private void validateCompleteRequestAndElseThrow(Order order, CompleteOrderDto request) {
        Optional<OrderStatusUpdate> updateWithCompletion = order.getLastUpdateWithStatus(OrderStatus.COMPLETED);
        if (updateWithCompletion.isPresent()) {
            throw OrderException.orderAlreadyCompletedException(order.getId());
        }
        Courier courierAssignedTo = order.getCourier();
        if (courierAssignedTo == null) {
            throw OrderException.orderNotAssignedException(order.getId(), request.courierId());
        } else if (courierAssignedTo.getId() != request.courierId()) {
            throw OrderException.orderAssignedToOtherCourierException(order.getId(), request.courierId());
        }
    }

    private Order getOrder(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> OrderException.orderNotFoundException(orderId));
    }
}
