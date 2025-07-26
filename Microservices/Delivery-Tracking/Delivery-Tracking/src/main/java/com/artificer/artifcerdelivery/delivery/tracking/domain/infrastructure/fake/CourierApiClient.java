package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.fake;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.CourierPayoutCalculationInput;
import com.artificer.artifcerdelivery.delivery.tracking.api.model.CourierPayoutResultModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/couriers")
public interface CourierApiClient {

    @PostExchange("/payout-calculation")
    @Retry(name = "Retry_CourierApiClient_payoutCalculation")
    @CircuitBreaker(name = "CircuitBreaker_CourierApiClient_payoutCalculation")
    CourierPayoutResultModel payoutCalculation(@RequestBody CourierPayoutCalculationInput payoutInputModel);
        // Placeholder for payout calculation logic
}
