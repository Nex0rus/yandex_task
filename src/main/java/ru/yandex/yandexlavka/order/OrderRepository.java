package ru.yandex.yandexlavka.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
