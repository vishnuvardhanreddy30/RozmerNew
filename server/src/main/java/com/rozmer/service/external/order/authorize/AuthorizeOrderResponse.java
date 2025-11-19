package com.rozmer.service.external.order.authorize;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizeOrderResponse {
    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("amount")
    private Double amount;

    // Getters and setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
