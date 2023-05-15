package ru.yandex.yandexlavka.order.components.group;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderGroupRepository extends JpaRepository<OrderGroup, Long> {
    OrderGroup findByCourierId(Long courierId);
}
