package ru.yandex.yandexlavka.common.exceptions;

public class NotFoundException extends DomainException {
    private NotFoundException(String message) {super(message);}

    public static NotFoundException courierNotFoundException(long courierId) {
        return new NotFoundException("Courier with specified id :" + courierId + " not found");
    }

    public static NotFoundException orderNotFoundException(long orderId) {
        return new NotFoundException("Order with specified id :" + orderId + " not found");
    }
}
