package com.rozmer.service.external.order.terminate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TerminateOrderRequest {
    @JsonProperty("order_status")
    private String orderStatus;

    public TerminateOrderRequest() {}

    public TerminateOrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    // Getters and setters
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
}
