package ru.yandex.yandexlavka.courier;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.common.types.PageRequestWithOffset;
import ru.yandex.yandexlavka.courier.dto.*;
import ru.yandex.yandexlavka.courier.dto.request.CreateCourierRequest;
import ru.yandex.yandexlavka.courier.dto.request.CreateCouriersResponse;
import ru.yandex.yandexlavka.courier.dto.response.CourierMetaInfoResponse;
import ru.yandex.yandexlavka.courier.dto.response.GetCouriersResponse;
import ru.yandex.yandexlavka.courier.service.CourierService;
import ru.yandex.yandexlavka.order.dto.response.OrderAssignResponse;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/couriers")
@RateLimiter(name = "rateLimiterApi")
public class CourierController {
    private final CourierService courierService;

    @Autowired
    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }
    @PostMapping("")
    public ResponseEntity<CreateCouriersResponse> createCouriers(@Valid @RequestBody CreateCourierRequest createCourierRequest) {
        List<CourierDto> createdCouriers = courierService.createCouriers(createCourierRequest);
        CreateCouriersResponse createCouriersResponse = new CreateCouriersResponse(createdCouriers);
        return ResponseEntity.ok(createCouriersResponse);
    }

    @GetMapping("")
    public ResponseEntity<GetCouriersResponse> getAllCouriers(
            @RequestParam(value = "offset", required = false, defaultValue = "0")
            @Min(value = 0, message = "offset of pagination must be a positive integer")
            Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "1")
            @Min(value = 1, message = "number of elements in a page must be at least 1")
            Integer limit)
    {
        PageRequestWithOffset pageRequest = PageRequestWithOffset.getInstance(offset, limit);
        List<CourierDto> couriersWithPagination  = courierService.getAllCouriers(pageRequest);
        GetCouriersResponse getCouriersResponse = GetCouriersResponse
                .builder()
                .couriers(couriersWithPagination)
                .limit(limit).offset(offset)
                .build();
        return ResponseEntity.ok(getCouriersResponse);
    }

    @GetMapping("/{courier_id}")
    public ResponseEntity<CourierDto> getCourierById(@Min(1) @PathVariable("courier_id") Long courierId) {
        CourierDto courierDto = courierService.getCourierById(courierId);
        return ResponseEntity.ok(courierDto);
    }

    @GetMapping("/meta-info/{courier_id}")
    public ResponseEntity<CourierMetaInfoResponse> getCourierMetaInfo(
            @Min(1) @PathVariable("courier_id") Long courierId,
            @NotNull @RequestParam(name = "startDate", required = true) LocalDate startDate,
            @NotNull @RequestParam(name="endDate", required = true) LocalDate endDate) {
        CourierMetaInfo metaInfo = courierService.getCourierMetaInfo(courierId, startDate, endDate);
        CourierMetaInfoResponse courierMetaInfoResponse = new CourierMetaInfoResponse(metaInfo);
        return ResponseEntity.ok(courierMetaInfoResponse);
    }

    @GetMapping("/assignments")
    public ResponseEntity<OrderAssignResponse> getOrderAssignments(
            @RequestParam(value = "date", defaultValue = "null", required = false) LocalDate date,
            @RequestParam(value = "courier_id", defaultValue = "null", required = false) Long courierId) {
        List<CouriersGroupOrders> couriersGroupOrdersList = courierService.getOrderAssignments(date, courierId);
        return ResponseEntity.ok(new OrderAssignResponse(date == null ? LocalDate.now() : date, couriersGroupOrdersList));
    }
}
