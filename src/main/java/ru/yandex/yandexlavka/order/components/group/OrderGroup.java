package ru.yandex.yandexlavka.order.components.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.yandexlavka.courier.model.Courier;
import ru.yandex.yandexlavka.order.model.Order;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "`GROUP`")
public class OrderGroup {
    public OrderGroup(Courier courier, LocalDate assignDate) {
        this.courier = courier;
        this.assignDate = assignDate;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "group_id")
    private Long id;

    @Getter
    @OneToMany(mappedBy="group")
    private final Set<Order> orders = new HashSet<>();

    @Getter
    @OneToOne(mappedBy = "orderGroup")
    private Courier courier;

    @Getter
    private LocalDate assignDate;
}
