package ru.yandex.yandexlavka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.yandexlavka.courier.components.type.*;

@Configuration
public class ApplicationConfiguration {
    @Autowired
    private CourierTypeRepository courierTypeRepository;
    @Bean
    public CourierTypeFactory courierTypeFactory() {
        CourierTypeFactoryImpl factoryImpl = new CourierTypeFactoryImpl();
        CourierTypeFactoryImpl.CourierTypeBuilder typeBuilder = new CourierTypeFactoryImpl.CourierTypeBuilder(CourierTypeEnum.FOOT);
        CourierType footType = typeBuilder
                .withRatingCoefficient(3)
                .withEarningsCoefficient(2)
                .withMaxOrderWeight(10)
                .withMaxOrderCount(2)
                .withMaxRegionsCount(1)
                .build();
        factoryImpl.addType(footType);
        CourierType bikeType = typeBuilder
                .resetToType(CourierTypeEnum.BIKE)
                .withRatingCoefficient(2)
                .withEarningsCoefficient(3)
                .withMaxOrderWeight(20)
                .withMaxOrderCount(4)
                .withMaxRegionsCount(2)
                .build();
        factoryImpl.addType(bikeType);
        CourierType autoType = typeBuilder
                .resetToType(CourierTypeEnum.AUTO)
                .withRatingCoefficient(1)
                .withEarningsCoefficient(4)
                .withMaxOrderWeight(40)
                .withMaxOrderCount(7)
                .withMaxRegionsCount(3)
                .build();
        factoryImpl.addType(autoType);
        courierTypeRepository.save(footType);
        courierTypeRepository.save(bikeType);
        courierTypeRepository.save(autoType);
        return factoryImpl;
    }
}
