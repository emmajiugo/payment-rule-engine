package com.emmajiugo.ruleImpl.mapper;

import lombok.Data;

@Data
public class FeeRuleMapper {
    private Condition condition;
    private Action action;

    @Data
    public static class Action{
        private String feeType;
        private double feeValue;
    }

    @Data
    public static class Condition{
        private String cardType;
        private String currency;
        private double maxAmount;
    }
}
