package ru.yandex.yandexlavka.courier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.yandex.yandexlavka.common.types.TimeInterval;
import ru.yandex.yandexlavka.courier.components.CourierTypeEnum;

import java.util.Set;
import java.util.stream.Collectors;

public class CourierDto {
    public  CourierDto(long courierId, CourierTypeEnum courierType, Set<Integer> regions, Set<TimeInterval> workingHours) {
        this.courierId = courierId;
        this.courierType = courierType;
        this.regions = regions;
        this.workingHours = workingHours.stream().map(TimeInterval::getStringInterval).collect(Collectors.toSet());
    }
    @Getter
    @JsonProperty("courier_id")
    private final  long courierId;
    @Getter
    @JsonProperty("courier_type")
    private final CourierTypeEnum courierType;
    @Getter
    private final Set<Integer> regions;
    @Getter
    @JsonProperty("working_hours")
    private final Set<String> workingHours;
}
