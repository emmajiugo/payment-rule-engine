package com.emmajiugo.ruleImpl.mapper;

import lombok.Data;

import java.util.List;

public class TransactionRouteMapper extends
        RuleMapper<TransactionRouteMapper.Condition, TransactionRouteMapper.Action> {

    @Data
    public static class Action{
        private String routingStrategy;
        private List<Route> routes;
    }

    @Data
    public static class Condition{
        public String cardScheme;
        private String currency;
    }

    @Data
    public static class Route{
        private String acquirer;
        private double percentage;
        private double min;
        private double max;
    }
}
