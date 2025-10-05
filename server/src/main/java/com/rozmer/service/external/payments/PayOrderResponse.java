package com.rozmer.service.external.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PayOrderResponse {

    @JsonProperty("payment_amount")
    private Double paymentAmount;

    @JsonProperty("cf_payment_id")
    private String cfPaymentId;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private String channel;

    private String action;

    private OrderPayData data;

    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("payment_id")
    private String paymentId;

    @Getter
    @Setter
    public static class OrderPayData {
        private String url;
        private Map<String, Object> payload;
        @JsonProperty("content_type")
        private String contentType;
        private String method;
    }
}
