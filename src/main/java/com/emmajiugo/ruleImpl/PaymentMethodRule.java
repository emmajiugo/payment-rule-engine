package com.emmajiugo.ruleImpl;

import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.dto.RuleDto;
import com.emmajiugo.utils.Utils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class PaymentMethodRule implements PaymentRule {
    private final List<RuleDto> rules;
    private List<String> supportedPaymentMethods;
    private List<String> defaultPaymentMethods;

    public PaymentMethodRule(String ruleMeta) {
        Type listType = new TypeToken<List<RuleDto>>() {}.getType();
        this.rules = Utils.fromJsonArray(ruleMeta, listType);
    }

    @Override
    public boolean evaluate(PaymentContext context) {
        for (RuleDto rule : rules) {
            String country = Utils.fromJson(Utils.toJson(rule.condition()), Map.class)
                    .get("country").toString();

            String supportedPaymentMethodsArray = Utils.fromJson(Utils.toJson(rule.action()), Map.class)
                    .get("supportedPaymentMethods").toString();

            Type stringType = new TypeToken<List<String>>() {}.getType();

            if (country.equalsIgnoreCase(context.getCountry())) {
                supportedPaymentMethods = Utils.fromJsonArray(supportedPaymentMethodsArray, stringType);
                return true;
            }

            if (country.equalsIgnoreCase("default")) {
                defaultPaymentMethods = Utils.fromJsonArray(supportedPaymentMethodsArray, stringType);
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

