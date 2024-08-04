package com.emmajiugo.ruleImpl;

import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.ruleImpl.mapper.PaymentMethodMapper;
import com.emmajiugo.utils.Utils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PaymentMethodRule implements PaymentRule {
    private final List<PaymentMethodMapper> ruleMapper;
    private List<String> supportedPaymentMethods;
    private List<String> defaultPaymentMethods;

    public PaymentMethodRule(String ruleMeta) {
        Type listType = new TypeToken<List<PaymentMethodMapper>>() {}.getType();
        this.ruleMapper = Utils.fromJsonArray(ruleMeta, listType);
    }

    @Override
    public boolean evaluate(PaymentContext context) {
        for (PaymentMethodMapper rule : ruleMapper) {
            String country = rule.getCondition().getCountry();

            if (country.equalsIgnoreCase(context.getCountry())) {
                supportedPaymentMethods = rule.getAction().getSupportedPaymentMethods();
                return true;
            }

            if (country.equalsIgnoreCase("default")) {
                defaultPaymentMethods = rule.getAction().getSupportedPaymentMethods();
            }
        }

        return true;
    }

    @Override
    public void execute(PaymentContext context) {
        List<String> paymentMethods = supportedPaymentMethods != null
                ? supportedPaymentMethods
                : defaultPaymentMethods;

        context.setPaymentMethods(paymentMethods);
    }
}

