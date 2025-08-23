package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.fake;

import com.artificer.artifcerdelivery.delivery.tracking.api.model.input.CourierPayoutCalculationInput;
import com.artificer.artifcerdelivery.delivery.tracking.api.model.output.CourierPayoutResultModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/couriers")
public interface CourierApiClient {

    @PostExchange("/payout-calculation")
    CourierPayoutResultModel payoutCalculation(@RequestBody CourierPayoutCalculationInput payoutInputModel);
}
