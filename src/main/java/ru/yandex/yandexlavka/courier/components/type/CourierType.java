package ru.yandex.yandexlavka.courier.components.type;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class CourierType {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Getter
    private CourierTypeEnum typeEnum;
    @Getter
    private int ratingCoefficient;
    @Getter
    private int earningsCoefficient;

    @Getter
    private int maxOrderWeight;
    @Getter
    private int maxOrderCount;

    @Getter
    private int maxRegionsCount;

    public CourierType(CourierTypeEnum typeEnum, int ratingCoefficient, int earningsCoefficient, int maxOrderWeight, int maxOrderCount, int maxRegionsCount) {
        this.typeEnum = typeEnum;
        this.ratingCoefficient = ratingCoefficient;
        this.earningsCoefficient = earningsCoefficient;
        this.maxOrderWeight = maxOrderWeight;
        this.maxOrderCount = maxOrderCount;
        this.maxRegionsCount = maxRegionsCount;
    }
}
