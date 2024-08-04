package com.emmajiugo.ruleImpl.mapper;

import com.emmajiugo.utils.Utils;
import lombok.Data;

import java.util.List;

public class HighRiskCountryMapper extends
        RuleMapper<HighRiskCountryMapper.Condition, HighRiskCountryMapper.Action> {

    @Data
    public static class Action{
        private boolean requireVerification;
    }

    @Data
    public static class Condition{
        private List<Country> countries;
    }

    @Data
    public static class Country{
        private String name;
        private Threshold threshold;
    }

    @Data
    public static class Threshold{
        private String currency;
        private int amount;
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
