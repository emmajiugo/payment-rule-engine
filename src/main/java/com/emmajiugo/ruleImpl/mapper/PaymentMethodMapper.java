package com.emmajiugo.ruleImpl.mapper;

import lombok.Data;

import java.util.List;

public class PaymentMethodMapper extends
        RuleMapper<PaymentMethodMapper.Condition, PaymentMethodMapper.Action> {

    @Data
    public static class Action{
        private List<String> supportedPaymentMethods;
    }

    @Data
    public static class Condition{
        private String country;
    }
}
