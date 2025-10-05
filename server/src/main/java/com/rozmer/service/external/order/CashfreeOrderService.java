package com.rozmer.service.external.order;

import com.rozmer.service.entity.Order;
import com.rozmer.service.repository.OrderRepository;
import com.rozmer.service.external.order.authorize.AuthorizeOrderRequest;
import com.rozmer.service.external.order.authorize.AuthorizeOrderResponse;
import com.rozmer.service.external.order.create.CreateOrderRequest;
import com.rozmer.service.external.order.create.CreateOrderResponse;
import com.rozmer.service.external.order.get.GetOrderResponse;
import com.rozmer.service.external.order.terminate.TerminateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CashfreeOrderService {

    @Value("${cashfree.client-id}")
    private String clientId;

    @Value("${cashfree.client-secret}")
    private String clientSecret;

    @Value("${cashfree.api-version}")
    private String apiVersion;

    private static final String CASHFREE_CREATE_ORDER_URL = "https://api.cashfree.com/pg/orders";
    private static final String CASHFREE_GET_ORDER_URL = "https://api.cashfree.com/pg/orders/{order_id}";
    private static final String CASHFREE_TERMINATE_ORDER_URL = "https://api.cashfree.com/pg/orders/{order_id}";
    private static final String CASHFREE_PREAUTH_URL = "https://api.cashfree.com/pg/orders/{order_id}/authorization";

    private final RestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    public CashfreeOrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CreateOrderResponse createOrder(CreateOrderRequest requestPayload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);
        headers.set("x-api-version", apiVersion);

        HttpEntity<CreateOrderRequest> entity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<CreateOrderResponse> resp;
        try {
            resp = restTemplate.exchange(
                    CASHFREE_CREATE_ORDER_URL,
                    HttpMethod.POST,
                    entity,
                    CreateOrderResponse.class
            );
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cashfree create order failed", ex);
        }

        if (resp.getStatusCode().is2xxSuccessful()) {
            CreateOrderResponse response = resp.getBody();
            if (response != null) {
                Order order = new Order();

                if (response.getOrderId() != null) {
                    order.setOrderId(response.getOrderId());
                }
                if (response.getOrderAmount() != null) {
                    order.setAmount(response.getOrderAmount());
                }
                if (response.getCfOrderId() != null) {
                    order.setCfOrderId(response.getCfOrderId());
                }
                if (response.getPaymentSessionId() != null) {
                    order.setPaymentSessionId(response.getPaymentSessionId());
                }
                if (response.getOrderCurrency() != null) {
                    order.setCurrency(response.getOrderCurrency());
                }
                if (response.getOrderStatus() != null) {
                    order.setStatus(response.getOrderStatus());
                }

                if (requestPayload.getCustomerDetails() != null) {
                    order.setCustomerEmail(requestPayload.getCustomerDetails().getCustomerEmail());
                    order.setCustomerPhone(requestPayload.getCustomerDetails().getCustomerPhone());
                    order.setCustomerName(requestPayload.getCustomerDetails().getCustomerName());
                    order.setCustomerId(requestPayload.getCustomerDetails().getCustomerId());
                }

                if (requestPayload.getOrderMeta() != null) {
                    order.setReturnUrl(requestPayload.getOrderMeta().getReturnUrl());
                    order.setNotifyUrl(requestPayload.getOrderMeta().getNotifyUrl());
                    order.setPaymentMethods(requestPayload.getOrderMeta().getPaymentMethods());
                }

                order.setOrderNote(requestPayload.getOrderNote());
                order.setCreatedAt(LocalDateTime.now());
                order.setUpdatedAt(LocalDateTime.now());

                try {
                    orderRepository.save(order);
                } catch (Exception ex) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save order to database", ex);
                }
            }
            return response;
        } else {
            throw new ResponseStatusException(resp.getStatusCode(), "Cashfree create order returned error: " + resp.getBody());
        }
    }

    public GetOrderResponse getOrder(String orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);
        headers.set("x-api-version", apiVersion);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GetOrderResponse> resp;
        try {
            resp = restTemplate.exchange(
                    CASHFREE_GET_ORDER_URL,
                    HttpMethod.GET,
                    entity,
                    GetOrderResponse.class,
                    orderId
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching order from Cashfree", e);
        }

        if (resp.getStatusCode().is2xxSuccessful()) {
            GetOrderResponse response = resp.getBody();

            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isPresent() && response != null) {
                Order order = orderOpt.get();
                if (response.getOrderStatus() != null) {
                    order.setStatus(response.getOrderStatus());
                }
                if (response.getOrderAmount() != null) {
                    order.setAmount(response.getOrderAmount());
                }
                if (response.getOrderCurrency() != null) {
                    order.setCurrency(response.getOrderCurrency());
                }
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
            }

            return response;
        } else {
            throw new ResponseStatusException(resp.getStatusCode(), "Cashfree returned non-2xx status: " + resp.getStatusCode());
        }
    }

    public GetOrderResponse terminateOrder(String orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);
        headers.set("x-api-version", apiVersion);

        TerminateOrderRequest body = new TerminateOrderRequest("TERMINATED");
        HttpEntity<TerminateOrderRequest> entity = new HttpEntity<>(body, headers);

        ResponseEntity<GetOrderResponse> resp;
        try {
            resp = restTemplate.exchange(
                    CASHFREE_TERMINATE_ORDER_URL,
                    HttpMethod.PATCH,
                    entity,
                    GetOrderResponse.class,
                    orderId
            );
        } catch (HttpStatusCodeException ex) {
            HttpStatus status = ex.getStatusCode();
            String responseBody = ex.getResponseBodyAsString();
            throw new ResponseStatusException(status, "Cashfree terminate order failed: " + responseBody, ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error calling Cashfree terminate order API", ex);
        }

        if (resp.getStatusCode().is2xxSuccessful()) {
            GetOrderResponse response = resp.getBody();

            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.setStatus("TERMINATED");
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
            }

            return response;
        } else {
            throw new ResponseStatusException(resp.getStatusCode(), "Non-2xx response from Cashfree: " + resp.getBody());
        }
    }

    public AuthorizeOrderResponse authorizeOrder(String orderId, AuthorizeOrderRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);
        headers.set("x-api-version", apiVersion);

        HttpEntity<AuthorizeOrderRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<AuthorizeOrderResponse> resp = restTemplate.exchange(
                    CASHFREE_PREAUTH_URL,
                    HttpMethod.POST,
                    entity,
                    AuthorizeOrderResponse.class,
                    orderId
            );

            AuthorizeOrderResponse response = resp.getBody();

            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isPresent() && response != null) {
                Order order = orderOpt.get();
                if (response.getPaymentId() != null) {
                    order.setPaymentId(response.getPaymentId());
                }
                if (response.getStatus() != null) {
                    order.setStatus(response.getStatus());
                }
                if (response.getAmount() != null) {
                    order.setAuthorizedAmount(response.getAmount());
                }
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
            }

            return response;
        } catch (HttpStatusCodeException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), "Preauthorization failed: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while calling Cashfree preauthorization", ex);
        }
    }
}
