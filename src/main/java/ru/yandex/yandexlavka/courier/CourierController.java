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
import ru.yandex.yandexlavka.courier.dto.CourierDto;
import ru.yandex.yandexlavka.courier.dto.CourierMetaInfoDto;
import ru.yandex.yandexlavka.courier.dto.CreateCourierDto;
import ru.yandex.yandexlavka.courier.service.CourierService;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/couriers")
@AllArgsConstructor
public class CourierController {
    @Autowired
    private final CourierService courierService;
    @PostMapping("")
    public ResponseEntity<List<CourierDto>> createCouriers(@Valid @RequestBody List<@Valid CreateCourierDto> createCourierRequests) {
        List<CourierDto> courierDtos = courierService.createCouriers(createCourierRequests);
        return ResponseEntity.ok(courierDtos);
    }

    @GetMapping("")
    public ResponseEntity<List<CourierDto>> getAllCouriers(
            @RequestParam(value = "offset", required = false, defaultValue = "0")
            @Min(value = 0, message = "offset of pagination must be a positive integer")
            int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "1")
            @Min(value = 1, message = "number of elements in a page must be at least 1")
            int limit)
    {
        PageRequestWithOffset pageRequest = PageRequestWithOffset.getInstance(offset, limit);
        List<CourierDto> courierDtos = courierService.getAllCouriers(pageRequest);
        return ResponseEntity.ok(courierDtos);
    }

    @GetMapping("/{courier_id}")
    public ResponseEntity<CourierDto> getCourierById(@Min(1) @PathVariable("courier_id") long courierId) {
        CourierDto courierDto = courierService.getCourierById(courierId);
        return ResponseEntity.ok(courierDto);
    }

    @GetMapping("/meta-info/{courier_id}")
    public ResponseEntity<CourierMetaInfoDto> getCourierMetaInfo(
            @Min(1) @PathVariable("courier_id") long courierId,
            @NotNull @RequestParam(name = "startDate", required = true) LocalDate startDate,
            @NotNull @RequestParam(name="endDate", required = true) LocalDate endDate) {
        CourierMetaInfoDto metaInfoDto = courierService.getCourierMetaInfo(courierId, startDate, endDate);
        return ResponseEntity.ok(metaInfoDto);
    }
}
