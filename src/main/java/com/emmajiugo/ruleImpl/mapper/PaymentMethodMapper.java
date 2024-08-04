package com.emmajiugo.ruleImpl.mapper;

import lombok.Data;

import java.util.List;

@Data
public class PaymentMethodMapper {
    private Condition condition;
    private Action action;

    @Data
    public static class Action{
        private List<String> supportedPaymentMethods;
    }

    @Data
    public static class Condition{
        private String country;
    }
}
