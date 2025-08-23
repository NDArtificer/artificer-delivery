package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.cache;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.output.CourierPayoutResultModel;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class cacheConfig {
    @Bean
    public Cache<Double, CourierPayoutResultModel> payoutCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(10))
                .maximumSize(1000)
                .build();
    }

}
