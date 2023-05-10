package ru.yandex.yandexlavka.common.exceptions;

import ru.yandex.yandexlavka.courier.components.CourierTypeEnum;

public class CourierException extends DomainException {
    private CourierException(String message) {
        super(message);
    }

    public static CourierException courierNotFoundException(long id) {
        return new CourierException("Failed to find courier with specified id : " + id);
    }

    public static CourierException illegalCourierTypeException(CourierTypeEnum typeVal) {
        return new CourierException("Provided illegal courier type + " + typeVal.toString());
    }

    public static CourierException courierTypeAlreadyConfigured(CourierTypeEnum typeVal) {
        return new CourierException("Provided courier type have already been configured : " + typeVal.toString());
    }
}
