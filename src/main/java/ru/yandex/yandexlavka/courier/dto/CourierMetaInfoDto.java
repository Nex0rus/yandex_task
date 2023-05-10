package ru.yandex.yandexlavka.courier.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;

public class CourierMetaInfoDto {
    private CourierMetaInfoDto(CourierDto courierDto, Integer rating, Integer earnings) {
        this.courierDto = courierDto;
        this.rating = rating;
        this.earnings = earnings;
    }

    public static CourierMetaInfoDto getInstance(CourierDto courierDto) {
        return new CourierMetaInfoDto(courierDto, null, null);
    }

    public static CourierMetaInfoDto getInstanceWithRatingAndEarnings(CourierDto courierDto, int rating, int earnings) {
        return new CourierMetaInfoDto(courierDto, rating, earnings);
    }
    @Getter
    @JsonUnwrapped
    private final CourierDto courierDto;
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer rating;
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer earnings;
}
