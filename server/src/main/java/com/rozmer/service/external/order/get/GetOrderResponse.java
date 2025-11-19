package com.rozmer.service.external.order.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrderResponse {
    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("order_amount")
    private Double orderAmount;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("order_currency")
    private String orderCurrency;
}
