package com.emmajiugo.ruleImpl.mapper;

import lombok.Data;

public class Required3DSMapper extends
        RuleMapper<Required3DSMapper.Condition, Required3DSMapper.Action> {

    @Data
    public static class Action{
        private boolean waive3DS;
    }

    @Data
    public static class Condition{
        private String customerType;
        private CheckPeriod checkPeriod;
    }

    @Data
    public static class CheckPeriod{
        private String unit;
        private long value;
    }
}
