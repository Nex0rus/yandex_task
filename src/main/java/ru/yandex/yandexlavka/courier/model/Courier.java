package ru.yandex.yandexlavka.courier.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.yandex.yandexlavka.common.types.TimeInterval;
import ru.yandex.yandexlavka.courier.components.CourierType;
import ru.yandex.yandexlavka.order.model.Order;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Courier {
    public Courier(CourierType type, Set<Integer> regions, Set<TimeInterval> workingHours) {
        this.type = type;
        this.regions = regions;
        this.workingHours = workingHours;
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

    @Getter
    @OneToMany(mappedBy="courier")
    private final Set<Order> orders = new HashSet<>();
}
