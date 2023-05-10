package ru.yandex.yandexlavka.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.yandex.yandexlavka.common.types.TimeInterval;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderDto {

    @Getter
    @JsonProperty("order_id")
    private final long orderId;
    @Getter
    private final int cost;
    @Getter
    private final float weight;
    @Getter
    private final int regions;
    @Getter
    @JsonProperty("delivery_hours")
    private final Set<String> deliveryHours;

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("complete_time")
    private final LocalDate completeTime;

    private OrderDto(Long orderId, int cost, float weight, int regions, Set<TimeInterval> deliveryHours, LocalDate completeTime) {
        this.orderId = orderId;
        this.cost = cost;
        this.weight = weight;
        this.regions = regions;
        this.deliveryHours = deliveryHours.stream().map(TimeInterval::getStringInterval).collect(Collectors.toSet());
        this.completeTime = completeTime;
    }

    public static OrderDto getInstance(Long orderId, int cost, float weight, int regions, Set<TimeInterval> deliveryHours) {
        return new OrderDto(orderId, cost, weight, regions, deliveryHours, null);
    }

    public static OrderDto getInstanceWithCompletedTime(Long orderId, int cost, float weight, int regions, Set<TimeInterval> deliveryHours, LocalDate completedTime) {
        return new OrderDto(orderId, cost, weight, regions, deliveryHours, completedTime);
    }
}