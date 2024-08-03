package com.emmajiugo.dto;

import com.emmajiugo.utils.Utils;
import lombok.Data;

import java.util.List;

@Data
public class PaymentContext {
    private String cardNumberMasked;
    private String cardType;
    private String cardScheme;
    private String currency;
    private double amount;
    private double fee;
    private String country;
    private String customerEmail;
    private boolean isEmployee;
    private String paymentRoute;
    private List<String> paymentMethods;
    private List<String> enabledFeatures;
    private boolean required3DS = true;
    private boolean requiredVerification;

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
