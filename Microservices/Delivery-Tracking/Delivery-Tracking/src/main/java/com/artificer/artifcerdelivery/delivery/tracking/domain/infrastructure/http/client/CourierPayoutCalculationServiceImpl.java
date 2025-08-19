package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.http.client;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.CourierPayoutCalculationInput;
import com.artificer.artifcerdelivery.delivery.tracking.api.model.CourierPayoutResultModel;
import com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.fake.CourierApiClient;
import com.artificer.artifcerdelivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import com.github.benmanes.caffeine.cache.Cache;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
@Slf4j
@Service
@RequiredArgsConstructor
public class CourierPayoutCalculationServiceImpl implements CourierPayoutCalculationService {

    private final CourierApiClient courierApiClient;
    private final Cache<Double, CourierPayoutResultModel> payoutCache;

    @Override
    @Retry(name = "Retry_CourierApiClient_payoutCalculation")
    @CircuitBreaker(name = "CircuitBreaker_CourierApiClient_payoutCalculation",  fallbackMethod = "fallbackPayout")
    public BigDecimal calculatePayout(Double distance) {

        try {
            var courierPayoutResultModel = courierApiClient.payoutCalculation(new CourierPayoutCalculationInput(distance));
            payoutCache.put(distance, courierPayoutResultModel);
            return courierPayoutResultModel.getPayoutFee();
        } catch (ResourceAccessException e) {
            throw new GatewayTimeOutException("Gateway timeout while calculating payout", e);
        } catch (HttpServerErrorException | CallNotPermittedException | IllegalArgumentException e) {
            throw new BadGatewayException("Bad gateway while calculating payout", e);
        }
    }

    public BigDecimal fallbackPayout(Double distance, Throwable throwable) {
        log.warn("Fallback ativado para distância {}. Motivo: {}", distance, throwable.getMessage());

        CourierPayoutResultModel cached = payoutCache.getIfPresent(distance);
        if (cached != null) {
            log.info("Retornando valor do cache para distância {}", distance);
            return cached.getPayoutFee();
        }

        log.info("Sem cache disponível. Retornando valor estimado.");
        return estimateFallbackFee(distance);
    }

    private BigDecimal estimateFallbackFee(Double distance) {
        return BigDecimal.valueOf(5.0 + (distance * 0.75));
    }
}
