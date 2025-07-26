package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.http.client;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.CourierPayoutCalculationInput;
import com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.fake.CourierApiClient;
import com.artificer.artifcerdelivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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
}
