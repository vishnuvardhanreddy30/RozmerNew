package com.rozmer.service.external.order.authorize;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizeOrderRequest {
    @JsonProperty("action")
    private String action; // capture or void

    @JsonProperty("amount")
    private Double amount;

    // Getters and setters
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
