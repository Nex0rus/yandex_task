package ru.yandex.yandexlavka.order;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.common.types.PageRequestWithOffset;
import ru.yandex.yandexlavka.courier.dto.CouriersGroupOrders;
import ru.yandex.yandexlavka.order.dto.*;
import ru.yandex.yandexlavka.order.dto.request.CompleteOrderRequest;
import ru.yandex.yandexlavka.order.dto.request.CreateOrderRequest;
import ru.yandex.yandexlavka.order.dto.response.OrderAssignResponse;
import ru.yandex.yandexlavka.order.service.OrderService;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/orders")
@RateLimiter(name = "rateLimiterApi")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<List<OrderDto>> createOrders(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        List<OrderDto> orderDtos = orderService.createOrders(createOrderRequest);
        return ResponseEntity.ok(orderDtos);
    }
    @GetMapping("")
    public ResponseEntity<List<OrderDto>> getAllOrders(
            @RequestParam(value = "offset", required = false, defaultValue = "0")
            @Min(value = 0, message = "offset of pagination must be a positive integer")
            int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "1")
            @Min(value = 1, message = "number of elements in a page must be at least 1")
            int limit)
    {
        PageRequestWithOffset pageRequest = PageRequestWithOffset.getInstance(offset, limit);
        List<OrderDto> orderDtos = orderService.getAllOrders(pageRequest);
        return ResponseEntity.ok(orderDtos);
    }

    @PostMapping("/complete")
    public ResponseEntity<List<OrderDto>> completeOrders(@Valid @RequestBody CompleteOrderRequest completeOrderRequest) {
        List<OrderDto> completedOrders = orderService.completeOrders(completeOrderRequest);
        return ResponseEntity.ok(completedOrders);
    }

    @PostMapping("/assign")
    public ResponseEntity<OrderAssignResponse> assignOrders(@NotNull @RequestParam("date")LocalDate date) {
        List<CouriersGroupOrders> couriersGroupOrdersList = orderService.assign(date);
        return ResponseEntity.ok(new OrderAssignResponse(date, couriersGroupOrdersList));
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("order_id") long orderId) {
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

}
