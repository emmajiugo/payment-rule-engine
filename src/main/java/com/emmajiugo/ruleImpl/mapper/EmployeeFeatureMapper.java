package com.emmajiugo.ruleImpl.mapper;

import lombok.Data;

import java.util.List;

public class EmployeeFeatureMapper extends
        RuleMapper<EmployeeFeatureMapper.Condition, EmployeeFeatureMapper.Action> {

    @Data
    public static class Action{
        public List<Feature> features;
    }

    @Data
    public static class Condition{
        public String requiredRole;
    }

    @Data
    public static class Feature{
        public String name;
        public boolean enabled;
    }
}
