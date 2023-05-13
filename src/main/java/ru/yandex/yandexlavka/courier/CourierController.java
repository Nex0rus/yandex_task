package ru.yandex.yandexlavka.courier;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.common.types.PageRequestWithOffset;
import ru.yandex.yandexlavka.courier.dto.*;
import ru.yandex.yandexlavka.courier.service.CourierService;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/couriers")
public class CourierController {
    private final CourierService courierService;
    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }
    @PostMapping("")
    public ResponseEntity<CreateCouriersResponse> createCouriers(@Valid @RequestBody CreateCourierRequest createCourierRequest) {
        CreateCouriersResponse createCouriersResponse = courierService.createCouriers(createCourierRequest);
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
        GetCouriersResponse  getCouriersResponse = courierService.getAllCouriers(pageRequest);
        return ResponseEntity.ok(getCouriersResponse);
    }

    @GetMapping("/{courier_id}")
    public ResponseEntity<CourierDto> getCourierById(@Min(1) @PathVariable("courier_id") Long courierId) {
        CourierDto courierDto = courierService.getCourierById(courierId);
        return ResponseEntity.ok(courierDto);
    }

    @GetMapping("/meta-info/{courier_id}")
    public ResponseEntity<CourierMetaInfoDto> getCourierMetaInfo(
            @Min(1) @PathVariable("courier_id") Long courierId,
            @NotNull @RequestParam(name = "startDate", required = true) LocalDate startDate,
            @NotNull @RequestParam(name="endDate", required = true) LocalDate endDate) {
        CourierMetaInfoDto metaInfoDto = courierService.getCourierMetaInfo(courierId, startDate, endDate);
        return ResponseEntity.ok(metaInfoDto);
    }
}
