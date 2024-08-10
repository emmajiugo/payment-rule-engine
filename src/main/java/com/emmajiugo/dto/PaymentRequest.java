package com.emmajiugo.dto;

import com.emmajiugo.utils.Utils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotBlank(message = "card masked span is required")
    private String cardNumberMasked;
    @NotBlank(message = "card type is required")
    private String cardType;
    @NotBlank(message = "card scheme is required")
    private String cardScheme;
    @NotBlank(message = "currency is required")
    private String currency;
    @NotNull(message = "amount is required")
    @Positive(message = "amount must not be negative")
    private double amount;
    @NotBlank(message = "country is required")
    private String country;
    @Email
    @NotBlank(message = "customer email is required")
    private String customerEmail;

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
