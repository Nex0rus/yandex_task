package ru.yandex.yandexlavka.courier.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.yandexlavka.common.types.TimeInterval;
import ru.yandex.yandexlavka.courier.components.type.CourierType;
import ru.yandex.yandexlavka.order.components.group.OrderGroup;
import ru.yandex.yandexlavka.order.model.Order;

import java.util.*;
import java.util.stream.Stream;

@Entity
@NoArgsConstructor
public class Courier {
    public Courier(CourierType type, Set<Integer> regions, Set<TimeInterval> workingHours) {
        this.regions = regions;
        this.type = type;
        this.workingHours = workingHours;
    }

//    public boolean canDeliverOrderWithDeliveryHours(Set<TimeInterval> deliveryHours) {
//        for (TimeInterval orderDeliveryInterval : deliveryHours) {
//            if (workingHours.stream().anyMatch(orderDeliveryInterval::belongsTo)) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    public Set<Order> getAllOrders() {
        return orderGroup == null ? Set.of() : orderGroup.getOrders();
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "courier_id")
    private Long id;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "courier_type_id", nullable = false)
    private CourierType type;
    @Getter
    @ElementCollection
    @CollectionTable(name = "courier_regions", joinColumns = @JoinColumn(name = "courier_id"))
    @Column(name = "region")
    private Set<Integer> regions;

    @Getter
    @ElementCollection
    @CollectionTable(name = "courier_working_hours", joinColumns = @JoinColumn(name = "courier_id"))
    @Column(name = "working_hours")
    private Set<TimeInterval> workingHours;

//    @Getter
//    @OneToMany(mappedBy="courier")
//    private final Set<OrderGroup> orderGroups = new HashSet<>();

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "courier_order_group",
            joinColumns =
                    { @JoinColumn(name = "courier_id")},
            inverseJoinColumns =
                    { @JoinColumn(name = "group_id") })
    private OrderGroup orderGroup;
}
