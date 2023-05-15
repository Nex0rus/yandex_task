package ru.yandex.yandexlavka.common.exceptions;

public class OrderException extends DomainException {
    private OrderException(String message) {
        super(message);
    }

    public static OrderException orderAlreadyCompletedException(long id) {
        return new OrderException("Failed to complete already completed order with specified id : " + id);
    }

    public static OrderException orderAssignedToOtherCourierException(long orderId, long wrongCourierId) {
        return new OrderException("Order with specified id : " + orderId + " was assigned to other courier.\nCan't be assigned to courier with id" + wrongCourierId);
    }

    public static OrderException orderNotAssignedException(long orderId, long wrongCourierId) {
        return new OrderException("Order with specified id : " + orderId + " wasn't assigned to any courier.\nCan't be completed by courier with id : " + wrongCourierId);
    }
}
