package com.emmajiugo.ruleImpl.mapper;

import lombok.Data;

public class FeeRuleMapper extends
        RuleMapper<FeeRuleMapper.Condition, FeeRuleMapper.Action> {

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
