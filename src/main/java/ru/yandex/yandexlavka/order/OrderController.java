package ru.yandex.yandexlavka.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.common.types.PageRequestWithOffset;
import ru.yandex.yandexlavka.order.dto.*;
import ru.yandex.yandexlavka.order.service.OrderService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<List<OrderDto>> createOrders(@RequestBody CreateOrderRequest createOrderRequest) {
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

    @GetMapping("/{order_id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("order_id") long orderId) {
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/complete")
    public ResponseEntity<List<OrderDto>> completeOrders(@RequestBody @Valid CompleteOrderRequest completeOrderRequest) {
        List<OrderDto> completedOrders = orderService.completeOrders(completeOrderRequest);
        return ResponseEntity.ok(completedOrders);
    }
}
