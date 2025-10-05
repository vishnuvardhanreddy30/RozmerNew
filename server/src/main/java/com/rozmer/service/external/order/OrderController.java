package com.rozmer.service.external.order;

import com.rozmer.service.external.order.authorize.AuthorizeOrderRequest;
import com.rozmer.service.external.order.authorize.AuthorizeOrderResponse;
import com.rozmer.service.external.order.create.CreateOrderRequest;
import com.rozmer.service.external.order.create.CreateOrderResponse;
import com.rozmer.service.external.order.get.GetOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cashfree")
@Validated
public class OrderController {

    @Autowired
    private CashfreeOrderService cashfreeOrderService;

    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest req) {
        CreateOrderResponse resp = cashfreeOrderService.createOrder(req);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/orders/{order_id}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable("order_id") String orderId) {
        GetOrderResponse response = cashfreeOrderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/orders/{order_id}/terminate")
    public ResponseEntity<GetOrderResponse> terminateOrder(@PathVariable("order_id") String orderId) {
        GetOrderResponse resp = cashfreeOrderService.terminateOrder(orderId);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/orders/{order_id}/authorization")
    public ResponseEntity<AuthorizeOrderResponse> authorizeOrder(
            @PathVariable("order_id") String orderId,
            @Valid @RequestBody AuthorizeOrderRequest request
    ) {
        AuthorizeOrderResponse resp = cashfreeOrderService.authorizeOrder(orderId, request);
        return ResponseEntity.ok(resp);
    }
}

