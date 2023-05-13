package ru.yandex.yandexlavka.order.components;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.yandex.yandexlavka.order.model.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class OrderStatusUpdate implements Comparable<OrderStatusUpdate> {
    public OrderStatusUpdate(Order order, OrderStatus status, LocalDateTime updateTime) {
        this.order = order;
        this.status = status;
        this.updateTime = updateTime;
    }
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long Id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Getter
    @Column(name = "status")
    private OrderStatus status;

    @Getter
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Override
    public int compareTo(OrderStatusUpdate o) {
        return updateTime.compareTo(o.getUpdateTime());
    }
}
