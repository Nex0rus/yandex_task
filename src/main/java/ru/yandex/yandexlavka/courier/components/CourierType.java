package ru.yandex.yandexlavka.courier.components;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
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

    public CourierType(CourierTypeEnum typeEnum, int ratingCoefficient, int earningsCoefficient) {
        this.typeEnum = typeEnum;
        this.ratingCoefficient = ratingCoefficient;
        this.earningsCoefficient = earningsCoefficient;
    }
}
