package com.rozmer.service.external.order.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderResponse {
    @JsonProperty("cf_order_id")
    private String cfOrderId;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("payment_session_id")
    private String paymentSessionId;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("order_currency")
    private String orderCurrency;

    @JsonProperty("order_amount")
    private Double orderAmount;
}
