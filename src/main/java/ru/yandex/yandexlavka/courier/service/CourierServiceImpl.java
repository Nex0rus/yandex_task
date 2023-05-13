package ru.yandex.yandexlavka.courier.service;

import lombok.experimental.ExtensionMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.common.exceptions.CourierException;
import ru.yandex.yandexlavka.courier.components.CourierExtensions;
import ru.yandex.yandexlavka.courier.components.CourierTypeFactory;
import ru.yandex.yandexlavka.courier.dto.*;
import ru.yandex.yandexlavka.courier.model.Courier;
import ru.yandex.yandexlavka.courier.CourierRepository;
import ru.yandex.yandexlavka.order.components.OrderStatus;
import ru.yandex.yandexlavka.order.components.OrderStatusUpdate;
import ru.yandex.yandexlavka.order.model.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@ExtensionMethod(CourierExtensions.class)
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;

    private final CourierTypeFactory courierTypeFactory;
    public CourierServiceImpl(CourierRepository courierRepository, CourierTypeFactory courierTypeFactory) {
        this.courierRepository = courierRepository;
        this.courierTypeFactory = courierTypeFactory;
    }
    @Override
    public CreateCouriersResponse createCouriers(CreateCourierRequest createCourierRequest) {
        List<Courier> couriers = createCourierRequest.couriers().stream()
                .map(ccr -> new Courier(courierTypeFactory.getTypeOf(ccr.type()), new HashSet<>(ccr.regions()), new HashSet<>(ccr.workingHours())))
                .toList();
        courierRepository.saveAll(couriers);
        return new CreateCouriersResponse(getCourierDtos(couriers));
    }

    @Override
    public GetCouriersResponse getAllCouriers(Pageable pageRequest) {
        List<CourierDto> courierDtos = getCourierDtos(courierRepository.findAll(pageRequest).getContent());
        return new GetCouriersResponse(courierDtos, pageRequest.getPageSize(), (int)pageRequest.getOffset());
    }

    @Override
    public CourierDto getCourierById(long courierId) {
        return getCourier(courierId).toDto();
    }

    @Override
    public CourierMetaInfoDto getCourierMetaInfo(long courierId, LocalDate startDate, LocalDate endDate) {
        Courier courier = getCourier(courierId);
        Set<Order> orders = courier.getOrders();
        List<Order> completedInInterval = orders.stream().filter(order ->
        {
            Optional<OrderStatusUpdate> completionUpdate = order.getLastUpdateWithStatus(OrderStatus.COMPLETED);
            if (completionUpdate.isEmpty()) {return false;}
            LocalDateTime completionTime = completionUpdate.get().getUpdateTime();

            return completionTime.toLocalDate().equals(startDate) || completionTime.isAfter(startDate.atStartOfDay()) && completionTime.isBefore(endDate.plusDays(1).atStartOfDay());
        }).toList();

        if (completedInInterval.size() == 0) {
            return CourierMetaInfoDto.getInstance(courier.toDto());
        }

        int rating = completedInInterval.size();
        int earnings = completedInInterval.stream().map(Order::getCost).reduce(0, (subtotal, cost) -> subtotal + cost * courier.getType().getEarningsCoefficient());
        return CourierMetaInfoDto.getInstanceWithRatingAndEarnings(courier.toDto(), rating, earnings);
    }

    private List<CourierDto> getCourierDtos(List<Courier> couriers) {
        return couriers.stream().map(courier -> courier.toDto()).toList();
    }

    private Courier getCourier(long courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> CourierException.courierNotFoundException(courierId));
    }
}
