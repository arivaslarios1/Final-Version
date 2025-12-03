package com.example.InvProject.controllers;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/billing")
public class BillingController {
  @Value("${stripe.api.key}") String apiKey;
  @Value("${stripe.price.id}") String priceId;
  @Value("${app.frontend.successUrl}") String successUrl;
  @Value("${app.frontend.cancelUrl}") String cancelUrl;

  @PostConstruct void init(){ Stripe.apiKey = apiKey; }

  // Create a Stripe Checkout session for subscription
  @PostMapping("/checkout-session")
  public ResponseEntity<Map<String,String>> createCheckout(@RequestBody Map<String, String> body) throws Exception {
    String email = body.getOrDefault("email", "customer@example.com");
    SessionCreateParams params = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
        .setSuccessUrl(successUrl)
        .setCancelUrl(cancelUrl)
        .addLineItem(SessionCreateParams.LineItem.builder()
            .setQuantity(1L)
            .setPrice(priceId)
            .build())
        .setCustomerEmail(email)
        .build();
    Session session = Session.create(params);
    return ResponseEntity.ok(Map.of("url", session.getUrl()));
  }
}