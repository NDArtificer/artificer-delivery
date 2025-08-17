package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.http.client;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.CourierPayoutCalculationInput;
import com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.fake.CourierApiClient;
import com.artificer.artifcerdelivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CourierPayoutCalculationServiceImpl implements CourierPayoutCalculationService {

    private final CourierApiClient courierApiClient;

    @Override
    @Retry(name = "Retry_CourierApiClient_payoutCalculation")
    @CircuitBreaker(name = "CircuitBreaker_CourierApiClient_payoutCalculation",  fallbackMethod = "fallbackPayout")
    public BigDecimal calculatePayout(Double distance) {

        try {
            var courierPayoutResultModel = courierApiClient.payoutCalculation(new CourierPayoutCalculationInput(distance));
            return courierPayoutResultModel.getPayoutFee();
        } catch (ResourceAccessException e) {
            throw new GatewayTimeOutException("Gateway timeout while calculating payout", e);
        } catch (HttpServerErrorException | CallNotPermittedException | IllegalArgumentException e) {
            throw new BadGatewayException("Bad gateway while calculating payout", e);
        }
    }

    public BigDecimal fallbackPayout(Double distance, Throwable throwable) {
        return BigDecimal.ZERO; // ou algum valor padrão
    }
}
