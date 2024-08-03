package com.emmajiugo.ruleImpl.mapper;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeFeatureMapper {
    private Condition condition;
    private Action action;

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
