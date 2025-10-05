package com.rozmer.service.external.order.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

@Getter
@Setter
public class CreateOrderRequest {
    @JsonProperty("order_id")
    @NotNull
    private String orderId;

    @JsonProperty("order_amount")
    @NotNull
    @Min(1)
    private Double orderAmount;

    @JsonProperty("order_currency")
    @NotNull
    private String orderCurrency;

    @JsonProperty("customer_details")
    private CustomerDetails customerDetails;

    @JsonProperty("order_meta")
    private OrderMeta orderMeta;

    @JsonProperty("order_note")
    private String orderNote;

    @Getter
    @Setter
    public static class CustomerDetails {
        @JsonProperty("customer_id")
        private String customerId;

        @JsonProperty("customer_name")
        private String customerName;

        @JsonProperty("customer_email")
        private String customerEmail;

        @JsonProperty("customer_phone")
        private String customerPhone;
    }

    @Getter
    @Setter
    public static class OrderMeta {
        @JsonProperty("return_url")
        private String returnUrl;

        @JsonProperty("notify_url")
        private String notifyUrl;

        @JsonProperty("payment_methods")
        private String paymentMethods;
    }
}
