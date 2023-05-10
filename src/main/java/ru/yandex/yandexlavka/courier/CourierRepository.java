package ru.yandex.yandexlavka.courier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.courier.model.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
}
