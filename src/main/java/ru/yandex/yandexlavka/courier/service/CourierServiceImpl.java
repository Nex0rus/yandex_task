package ru.yandex.yandexlavka.courier.service;

import jakarta.transaction.Transactional;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.common.exceptions.NotFoundException;
import ru.yandex.yandexlavka.courier.components.CourierExtensions;
import ru.yandex.yandexlavka.courier.components.type.CourierTypeFactory;
import ru.yandex.yandexlavka.courier.dto.*;
import ru.yandex.yandexlavka.courier.dto.request.CreateCourierRequest;
import ru.yandex.yandexlavka.courier.model.Courier;
import ru.yandex.yandexlavka.courier.CourierRepository;
import ru.yandex.yandexlavka.order.components.group.OrderGroup;
import ru.yandex.yandexlavka.order.components.group.OrderGroupExtensions;
import ru.yandex.yandexlavka.order.components.group.OrderGroupRepository;
import ru.yandex.yandexlavka.order.components.status.OrderStatus;
import ru.yandex.yandexlavka.order.components.status.OrderStatusUpdate;
import ru.yandex.yandexlavka.order.model.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@ExtensionMethod({CourierExtensions.class, OrderGroupExtensions.class})
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;

    private final CourierTypeFactory courierTypeFactory;

    private final OrderGroupRepository orderGroupRepository;

    @Autowired
    public CourierServiceImpl(CourierRepository courierRepository, CourierTypeFactory courierTypeFactory, OrderGroupRepository orderGroupRepository) {
        this.courierRepository = courierRepository;
        this.courierTypeFactory = courierTypeFactory;
        this.orderGroupRepository = orderGroupRepository;
    }
    @Override
    public List<CourierDto> createCouriers(CreateCourierRequest createCourierRequest) {
        List<Courier> couriers = createCourierRequest.couriers().stream()
                .map(ccr -> new Courier(courierTypeFactory.getTypeOf(ccr.type()), new HashSet<>(ccr.regions()), new HashSet<>(ccr.workingHours())))
                .toList();
        courierRepository.saveAll(couriers);
        return getCourierDtos(couriers);
    }

    @Override
    @Transactional
    public List<CourierDto> getAllCouriers(Pageable pageRequest) {
        return getCourierDtos(courierRepository.findAll(pageRequest).getContent());
    }

    @Override
    @Transactional
    public CourierDto getCourierById(long courierId) {
        return getCourier(courierId).toDto();
    }

    @Override
    @Transactional
    public CourierMetaInfo getCourierMetaInfo(long courierId, LocalDate startDate, LocalDate endDate) {
        Courier courier = getCourier(courierId);
        Set<Order> orders = courier.getAllOrders();
        List<Order> completedInInterval = getOrdersCompletedInInterval(orders, startDate, endDate);

        if (completedInInterval.isEmpty()) {
            return CourierMetaInfo.getEmptyInstance(startDate, endDate, courier.toDto());
        }
        long hours = ChronoUnit.HOURS.between(startDate, endDate);
        int rating = (int)(completedInInterval.size() / hours) * courier.getType().getRatingCoefficient();
        int earnings = completedInInterval
                .stream()
                .map(Order::getCost)
                .reduce(0,
                        (subtotal, cost) ->
                                subtotal + cost * courier.getType().getEarningsCoefficient()
                );

        return CourierMetaInfo.getInstanceWithMetaInfo(startDate, endDate, courier.toDto(), earnings, rating);
    }

    @Override
    public List<CouriersGroupOrders> getOrderAssignments(LocalDate date, Long courierId) {
        List<OrderGroup> groups;
        if (courierId != null) {
            groups = List.of(orderGroupRepository.findByCourierId(courierId));
        } else {
            groups = orderGroupRepository.findAll();
        }

        List<OrderGroup> groupsWithDate = groups.stream()
                .filter(group -> group.getAssignDate().equals(date == null ? LocalDate.now() : date)).toList();
        return groupsWithDate.stream().map(group -> group.getCourier().toCourierGroupOrdersDto()).toList();
    }


    private List<CourierDto> getCourierDtos(List<Courier> couriers) {
        return couriers.stream().map(courier -> courier.toDto()).toList();
    }

    private Courier getCourier(long courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> NotFoundException.courierNotFoundException(courierId));
    }

    private List<Order> getOrdersCompletedInInterval(Collection<Order> orders, LocalDate startDate, LocalDate endDate) {
        return orders.stream().filter(order ->
        {
            Optional<OrderStatusUpdate> completionUpdate = order.getLastUpdateWithStatus(OrderStatus.COMPLETED);
            if (completionUpdate.isEmpty()) {return false;}
            LocalDateTime completionTime = completionUpdate.get().getUpdateTime();

            return completionTime.equals(startDate.atStartOfDay())
                    || (completionTime.isAfter(startDate.atStartOfDay()) && completionTime.isBefore(endDate.atStartOfDay()));
        }).toList();
    }
}
