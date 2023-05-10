package ru.yandex.yandexlavka.courier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.common.exceptions.CourierException;
import ru.yandex.yandexlavka.courier.components.CourierTypeFactory;
import ru.yandex.yandexlavka.courier.dto.CourierMetaInfoDto;
import ru.yandex.yandexlavka.courier.model.Courier;
import ru.yandex.yandexlavka.courier.CourierRepository;
import ru.yandex.yandexlavka.courier.dto.CourierDto;
import ru.yandex.yandexlavka.courier.dto.CreateCourierDto;
import ru.yandex.yandexlavka.order.components.OrderStatus;
import ru.yandex.yandexlavka.order.components.OrderStatusUpdate;
import ru.yandex.yandexlavka.order.model.Order;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CourierServiceImpl implements CourierService {
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private CourierTypeFactory courierTypeFactory;
    @Override
    public List<CourierDto> createCouriers(List<CreateCourierDto> createCourierRequests) {
        List<Courier> couriers = createCourierRequests.stream()
                .map(ccr -> new Courier(courierTypeFactory.getTypeOf(ccr.type()), new HashSet<>(ccr.regions()), new HashSet<>(ccr.workingHours())))
                .toList();
        courierRepository.saveAll(couriers);
        return getCourierDtos(couriers);
    }

    @Override
    public List<CourierDto> getAllCouriers(Pageable pageRequest) {
        return getCourierDtos(courierRepository.findAll(pageRequest).getContent());
    }

    @Override
    public CourierDto getCourierById(long courierId) {
        return toCourierDto(getCourier(courierId));
    }

    @Override
    public CourierMetaInfoDto getCourierMetaInfo(long courierId, LocalDate startDate, LocalDate endDate) {
        Courier courier = getCourier(courierId);
        Set<Order> orders = courier.getOrders();
        List<Order> completedInInterval = orders.stream().filter(order ->
        {
            Optional<OrderStatusUpdate> completionUpdate = order.getLastUpdateWithStatus(OrderStatus.COMPLETED);
            if (completionUpdate.isEmpty()) {return false;}
            LocalDate completionTime = completionUpdate.get().getUpdateTime();

            return completionTime.equals(startDate) || completionTime.isAfter(startDate) && completionTime.isBefore(endDate);
        }).toList();

        if (completedInInterval.size() == 0) {
            return CourierMetaInfoDto.getInstance(toCourierDto(courier));
        }

        int rating = completedInInterval.size();
        int earnings = completedInInterval.stream().map(Order::getCost).reduce(0, (subtotal, cost) -> subtotal + cost * courier.getType().getEarningsCoefficient());
        return CourierMetaInfoDto.getInstanceWithRatingAndEarnings(toCourierDto(courier), rating, earnings);
    }

    private List<CourierDto> getCourierDtos(List<Courier> couriers) {
        return couriers.stream().map(this::toCourierDto).toList();
    }

    private CourierDto toCourierDto(Courier courier) {
        return new CourierDto(courier.getId(), courier.getType().getTypeEnum(), courier.getRegions(), courier.getWorkingHours());
    }

    private Courier getCourier(long courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> CourierException.courierNotFoundException(courierId));
    }
}
