package ru.yandex.yandexlavka.courier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@AllArgsConstructor
public class CourierMetaInfo {
    @Getter
    private LocalDate timeIntervalStart;
    @Getter
    private LocalDate timeIntervalEnd;
    @Getter
    private CourierDto courierDto;
    @Getter
    private Integer earnings;
    @Getter
    private Integer rating;

    public static CourierMetaInfo getEmptyInstance(LocalDate timeIntervalStart, LocalDate timeIntervalEnd, CourierDto courierDto) {
        return new CourierMetaInfo(timeIntervalStart, timeIntervalEnd, courierDto,null, null);
    }

    public static CourierMetaInfo getInstanceWithMetaInfo(LocalDate timeIntervalStart, LocalDate timeIntervalEnd, CourierDto courierDto, Integer earnings, Integer rating) {
        return new CourierMetaInfo(timeIntervalStart, timeIntervalEnd, courierDto, earnings, rating);
    }
}
