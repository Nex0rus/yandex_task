package ru.yandex.yandexlavka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.yandexlavka.courier.components.*;

@Configuration
public class ApplicationConfiguration {
    @Autowired
    private CourierTypeRepository courierTypeRepository;
    @Bean
    public CourierTypeFactory courierTypeFactory() {
        CourierTypeFactoryImpl factoryImpl = new CourierTypeFactoryImpl();
        CourierTypeFactoryImpl.CourierTypeBuilder typeBuilder = new CourierTypeFactoryImpl.CourierTypeBuilder(CourierTypeEnum.FOOT);
        CourierType footType = typeBuilder.withRatingCoefficient(3).withEarningsCoefficient(2).build();
        factoryImpl.addType(footType);
        CourierType bikeType = typeBuilder.resetToType(CourierTypeEnum.BIKE).withRatingCoefficient(2).withEarningsCoefficient(3).build();
        factoryImpl.addType(bikeType);
        CourierType autoType = typeBuilder.resetToType(CourierTypeEnum.AUTO).withRatingCoefficient(1).withEarningsCoefficient(4).build();
        factoryImpl.addType(autoType);
        courierTypeRepository.save(footType);
        courierTypeRepository.save(bikeType);
        courierTypeRepository.save(autoType);
        return factoryImpl;
    }
}
