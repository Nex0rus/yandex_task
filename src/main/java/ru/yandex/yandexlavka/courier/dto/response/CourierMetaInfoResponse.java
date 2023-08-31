package ru.yandex.yandexlavka.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import ru.yandex.yandexlavka.courier.dto.CourierDto;
import ru.yandex.yandexlavka.courier.dto.CourierMetaInfo;

public class CourierMetaInfoResponse {
    public CourierMetaInfoResponse(CourierMetaInfo courierMetaInfo) {
        this.courierDto = courierMetaInfo.getCourierDto();
        this.rating = courierMetaInfo.getRating();
        this.earnings = courierMetaInfo.getEarnings();
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
