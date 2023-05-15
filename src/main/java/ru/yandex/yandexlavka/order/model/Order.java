package ru.yandex.yandexlavka.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.yandexlavka.common.types.TimeInterval;
import ru.yandex.yandexlavka.courier.model.Courier;
import ru.yandex.yandexlavka.order.components.group.OrderGroup;
import ru.yandex.yandexlavka.order.components.status.OrderStatus;
import ru.yandex.yandexlavka.order.components.status.OrderStatusUpdate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;

@Entity
@NoArgsConstructor
@Table(name = "`ORDER`")
public class Order {

    public Order(int cost, float weight, int region, Set<TimeInterval> deliveryHours) {
        this.cost = cost;
        this.weight = weight;
        this.region = region;
        this.deliveryHours = deliveryHours;
    }
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    private Long id;
    @Getter
    private int cost;
    @Getter
    private float weight;
    @Getter
    private int region;
    @Getter
    @ElementCollection
    @CollectionTable(name = "order_delivery_hours", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "delivery_hours")
    private Set<TimeInterval> deliveryHours = new HashSet<>();
    @Getter
    @OneToMany(mappedBy="order")
    private final Set<OrderStatusUpdate> updateHistory = new HashSet<>();
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private OrderGroup group;

    public Optional<OrderStatusUpdate> getLastUpdateWithStatus(OrderStatus status) {
        return getUpdateHistory()
                .stream()
                .filter(orderStatusUpdate -> orderStatusUpdate.getStatus().equals(status))
                .max(OrderStatusUpdate::compareTo);
    }

    public OrderStatusUpdate getLastUpdate() {
        return getUpdateHistory()
                .stream()
                .max(OrderStatusUpdate::compareTo).get();
    }
}
