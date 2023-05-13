package ru.yandex.yandexlavka.courier.components;

import ru.yandex.yandexlavka.common.exceptions.CourierException;

import java.util.HashMap;
import java.util.Map;

public class CourierTypeFactoryImpl implements CourierTypeFactory {
    private final Map<CourierTypeEnum, CourierType> storedTypes = new HashMap<>();
    @Override
    public CourierType getTypeOf(CourierTypeEnum courierTypeEnum) {
        CourierType concreteType = storedTypes.get(courierTypeEnum);
        if (concreteType != null) {
            return concreteType;
        }

        throw CourierException.illegalCourierTypeException(courierTypeEnum);
    }

    public void addType(CourierType type) {
        if (storedTypes.get(type.getTypeEnum()) != null) {
            throw CourierException.courierTypeAlreadyConfigured(type.getTypeEnum());
        }
        storedTypes.put(type.getTypeEnum(), type);
    }

    public static class CourierTypeBuilder {
        private CourierTypeEnum typeEnumVal;
        private int ratingCoefficient = 0;
        private int earningsCoefficient = 0;

        public CourierTypeBuilder(CourierTypeEnum typeEnumVal) {
            this.typeEnumVal = typeEnumVal;
        }

        public CourierTypeBuilder withRatingCoefficient(int ratingCoefficient) {
            this.ratingCoefficient = ratingCoefficient;
            return this;
        }

        public CourierTypeBuilder withEarningsCoefficient(int earningsCoefficient) {
            this.earningsCoefficient = earningsCoefficient;
            return this;
        }

        public CourierType build() {
            return new CourierType(typeEnumVal, ratingCoefficient, earningsCoefficient);
        }

        public CourierTypeBuilder resetToType(CourierTypeEnum typeEnumVal) {
            this.typeEnumVal = typeEnumVal;
            this.ratingCoefficient = 0;
            this.earningsCoefficient = 0;
            return this;
        }
    }
}
