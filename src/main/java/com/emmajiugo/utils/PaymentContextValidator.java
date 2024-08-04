package com.emmajiugo.utils;

import com.emmajiugo.dto.PaymentContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Simple validation
public class PaymentContextValidator {
    public static Error validate(PaymentContext paymentContext) throws IllegalArgumentException {
        List<String> missingFields = new ArrayList<>();

        if (paymentContext.getCardNumberMasked() == null || paymentContext.getCardNumberMasked().isEmpty()) {
            missingFields.add("'cardNumberMasked' is required.");
        }
        if (paymentContext.getCardType() == null || paymentContext.getCardType().isEmpty()) {
            missingFields.add("'cardType' is required.");
        }
        if (paymentContext.getCardScheme() == null || paymentContext.getCardScheme().isEmpty()) {
            missingFields.add("'cardScheme' is required.");
        }
        if (paymentContext.getCurrency() == null || paymentContext.getCurrency().isEmpty()) {
            missingFields.add("'currency' is required.");
        }
        if (paymentContext.getAmount() <= 0) {
            missingFields.add("'amount' is required.");
        }
        if (paymentContext.getCountry() == null || paymentContext.getCountry().isEmpty()) {
            missingFields.add("'country' is required.");
        }
        if (paymentContext.getCustomerEmail() == null || paymentContext.getCustomerEmail().isEmpty()) {
            missingFields.add("'customerEmail' is required.");
        }

        if (!missingFields.isEmpty()) {
            return new Error("VALIDATION", "Missing required fields", missingFields);
        }

        return null;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Error {
        private String type;
        private String message;
        private Object errors;
    }
}
