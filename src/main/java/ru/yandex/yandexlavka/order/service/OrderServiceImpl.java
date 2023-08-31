package ru.yandex.yandexlavka.order.service;

import jakarta.transaction.Transactional;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.common.exceptions.NotFoundException;
import ru.yandex.yandexlavka.common.exceptions.OrderException;
import ru.yandex.yandexlavka.courier.CourierRepository;
import ru.yandex.yandexlavka.courier.components.CourierExtensions;
import ru.yandex.yandexlavka.courier.dto.CouriersGroupOrders;
import ru.yandex.yandexlavka.courier.model.Courier;
import ru.yandex.yandexlavka.order.components.*;
import ru.yandex.yandexlavka.order.components.group.OrderGroup;
import ru.yandex.yandexlavka.order.components.group.OrderGroupExtensions;
import ru.yandex.yandexlavka.order.components.group.OrderGroupRepository;
import ru.yandex.yandexlavka.order.components.status.OrderStatus;
import ru.yandex.yandexlavka.order.components.status.OrderStatusUpdate;
import ru.yandex.yandexlavka.order.components.status.OrderStatusUpdateRepository;
import ru.yandex.yandexlavka.order.dto.*;
import ru.yandex.yandexlavka.order.dto.request.CompleteOrderDto;
import ru.yandex.yandexlavka.order.dto.request.CompleteOrderRequest;
import ru.yandex.yandexlavka.order.dto.request.CreateOrderRequest;
import ru.yandex.yandexlavka.order.model.Order;
import ru.yandex.yandexlavka.order.OrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@ExtensionMethod({OrderExtensions.class, OrderGroupExtensions.class, CourierExtensions.class})
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderStatusUpdateRepository orderStatusUpdateRepository;

    private final CourierRepository courierRepository;

    private final OrderGroupRepository orderGroupRepository;


    @Autowired

    public OrderServiceImpl(OrderRepository orderRepository, OrderStatusUpdateRepository orderStatusUpdateRepository, CourierRepository courierRepository, OrderGroupRepository orderGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderStatusUpdateRepository = orderStatusUpdateRepository;
        this.courierRepository = courierRepository;
        this.orderGroupRepository = orderGroupRepository;
    }


    @Override
    @Transactional
    public List<OrderDto> createOrders(CreateOrderRequest createOrderRequest) {
        List<Order> orders = createOrderRequest.orders().stream()
                .map(request ->
                {
                    Order newOrder = new Order(request.cost(), request.weight(), request.regions(), new HashSet<>(request.deliveryHours()));
                    orderRepository.save(newOrder);
                    OrderStatusUpdate newOrderUpdate = new OrderStatusUpdate(newOrder, OrderStatus.NEW, LocalDateTime.now());
                    newOrder.getUpdateHistory().add(newOrderUpdate);
                    orderStatusUpdateRepository.save(newOrderUpdate);
                    return newOrder;
                }
                ).toList();
        return getOrderDtos(orders);
    }

    @Override
    @Transactional
    public List<OrderDto> getAllOrders(Pageable pageRequest) {
        return getOrderDtos(orderRepository.findAll(pageRequest).getContent());
    }

    @Override
    @Transactional
    public OrderDto getOrderById(long orderId) {
        return getOrder(orderId).toDto();
    }

    @Override
    @Transactional
    public List<OrderDto> completeOrders(CompleteOrderRequest completeOrderRequest) {
        List<OrderDto> orderDtos = new ArrayList<>();
        completeOrderRequest.completeInfo().forEach(request -> {
            Order order = getOrder(request.orderId());
            tryUpdateOrderStatusOrElseThrow(order, request);
            orderDtos.add(order.toDto());
        });

        return orderDtos;
    }

    @Override
    @Transactional
    public List<CouriersGroupOrders> assign(LocalDate assignDate) {
        List<Order> newOrders = orderRepository.findAll()
                .stream()
                .filter(order -> order.getLastUpdate().getStatus().equals(OrderStatus.NEW))
                .sorted(Comparator.comparingInt(Order::getCost))
                .toList();

        Set<Order> sortedOrders = new LinkedHashSet<>(newOrders);
        List<Courier> allCouriers = courierRepository.findAll();
        Set<Courier> couriers = new HashSet<>(allCouriers);
        sortedOrders.forEach( order ->
        {
            Set<Courier> possibleCouriers = getPossibleCouriers(order, couriers);
            Optional<Courier> possibleCourierAssignTo = getPossibleCourierAssignTo(possibleCouriers, order);
            if (possibleCourierAssignTo.isPresent()) {
                Courier courierAssignTo = possibleCourierAssignTo.get();
                OrderStatusUpdate assignedStatusUpdate = new OrderStatusUpdate(order, OrderStatus.ASSIGNED, assignDate.atStartOfDay());
                order.getUpdateHistory().add(assignedStatusUpdate);
                orderStatusUpdateRepository.save(assignedStatusUpdate);
                if (courierAssignTo.getOrderGroup() == null) {
                    OrderGroup newGroup = new OrderGroup(courierAssignTo, assignDate);
                    courierAssignTo.setOrderGroup(newGroup);
                    courierRepository.save(courierAssignTo);
                }
                OrderGroup orderGroup = courierAssignTo.getOrderGroup();
                orderGroup.getOrders().add(order);
                order.setGroup(orderGroup);
                orderGroupRepository.save(orderGroup);
                orderRepository.save(order);
                if (orderGroup.getOrders().size() == courierAssignTo.getType().getMaxOrderCount()) {
                    couriers.remove(courierAssignTo);
                }
            }
        });

        return allCouriers.stream().map(courier -> courier.toCourierGroupOrdersDto()).toList();
    }

    private Optional<Courier> getPossibleCourierAssignTo(Set<Courier> possibleCouriers, Order currentOrder) {
        Function<Courier, Stream<Integer>> getCurrentRegionsOfAssignedOrders = (Courier c) ->
        {
            List<Integer> allAssignedRegions = c.getAllOrders().stream().map(Order::getRegion).collect(Collectors.toList());
            allAssignedRegions.add(currentOrder.getRegion());
            return allAssignedRegions.stream().distinct();
        };

        Function<Courier, Long> getNumOfRegionsLeft = (Courier c) ->
                c.getType().getMaxRegionsCount() - getCurrentRegionsOfAssignedOrders.apply(c).count();

        Comparator<Courier> courierRegionsLeftDescendingComparator = Comparator.comparing(getNumOfRegionsLeft);

        return possibleCouriers.stream().max(courierRegionsLeftDescendingComparator);
    }

    private List<OrderDto> getOrderDtos(List<Order> orders) {
        return orders.stream().map(order -> order.toDto()).toList();
    }

    private void tryUpdateOrderStatusOrElseThrow(Order order, CompleteOrderDto request) {
        Optional<OrderStatusUpdate> updateWithCompletion = order.getLastUpdateWithStatus(OrderStatus.COMPLETED);
        if (updateWithCompletion.isPresent()) {
            return;
        }
        Courier courierAssignedTo = order.getGroup().getCourier();
        if (courierAssignedTo == null) {
            throw OrderException.orderNotAssignedException(order.getId(), request.courierId());
        } else if (!courierAssignedTo.getId().equals(request.courierId())) {
            throw OrderException.orderAssignedToOtherCourierException(order.getId(), request.courierId());
        }
        OrderStatusUpdate completionStatusUpdate = new OrderStatusUpdate(order, OrderStatus.COMPLETED, request.completeTime());
        order.getUpdateHistory().add(completionStatusUpdate);
    }

    private Order getOrder(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> NotFoundException.orderNotFoundException(orderId));
    }

    private Set<Courier> getPossibleCouriers(Order order, Set<Courier> couriersLeft) {
        Predicate<Courier> courierCanCarryOrderWeight = (Courier courier) -> courier.getType().getMaxOrderWeight() >= order.getWeight();
        Predicate<Courier> courierWorksInOrderRegion = (Courier courier) -> courier.getRegions().contains(order.getRegion());
        return couriersLeft.stream()
                .filter(courierCanCarryOrderWeight)
                .filter(courierWorksInOrderRegion).collect(Collectors.toSet());
    }
}
