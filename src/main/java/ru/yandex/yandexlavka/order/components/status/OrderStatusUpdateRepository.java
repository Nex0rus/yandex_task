package ru.yandex.yandexlavka.order.components.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusUpdateRepository extends JpaRepository<OrderStatusUpdate, Long> {
}
