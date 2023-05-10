package ru.yandex.yandexlavka.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.common.types.PageRequestWithOffset;
import ru.yandex.yandexlavka.order.dto.CompleteOrderDto;
import ru.yandex.yandexlavka.order.dto.CreateOrderDto;
import ru.yandex.yandexlavka.order.dto.OrderDto;
import ru.yandex.yandexlavka.order.service.OrderService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("")
    ResponseEntity<List<OrderDto>> createOrders(@RequestBody List<@Valid CreateOrderDto> createOrderRequests) {
        List<OrderDto> orderDtos = orderService.createOrders(createOrderRequests);
        return ResponseEntity.ok(orderDtos);
    }
    @GetMapping("")
    ResponseEntity<List<OrderDto>> getAllOrders(
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
    ResponseEntity<OrderDto> getOrderById(@PathVariable("order_id") long orderId) {
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/complete")
    ResponseEntity<List<OrderDto>> completeOrders(@RequestBody List<@Valid CompleteOrderDto> completeOrderRequests) {
        List<OrderDto> completedOrders = orderService.completeOrders(completeOrderRequests);
        return ResponseEntity.ok(completedOrders);
    }
}
