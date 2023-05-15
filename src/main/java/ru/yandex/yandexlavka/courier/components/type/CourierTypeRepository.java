package ru.yandex.yandexlavka.courier.components.type;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.yandexlavka.courier.components.type.CourierType;

public interface CourierTypeRepository extends JpaRepository<CourierType, Long> {
}
