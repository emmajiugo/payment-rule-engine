package com.emmajiugo.ruleImpl;

import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.dto.RuleDto;
import com.emmajiugo.utils.Utils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TransactionRouteRule implements PaymentRule {
    private final List<RuleDto> rules;
    private Random random = new Random();
    private RuleDto matchedRule = null;

    public TransactionRouteRule(String ruleMeta) {
        Type listType = new TypeToken<List<RuleDto>>() {}.getType();
        this.rules = Utils.fromJsonArray(ruleMeta, listType);
    }

    @Override
    public boolean evaluate(PaymentContext context) {
        for (RuleDto rule : rules) {
            var condition = Utils.fromJson(Utils.toJson(rule.condition()), Map.class);

            String cardScheme = (String) condition.get("cardScheme");
            String currency = (String) condition.get("currency");

            if (cardScheme.equalsIgnoreCase(context.getCardScheme())
                    && currency.equalsIgnoreCase(context.getCurrency())) {
                this.matchedRule = rule;
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(PaymentContext context) {
        if (matchedRule == null) {
            throw new RuntimeException("No matched rule found for currency: " + context.getCurrency()
                    + " and cardType: " + context.getCardType());
        }

        var action = Utils.fromJson(Utils.toJson(matchedRule.action()), Map.class);

        String routingStrategy = (String) action.get("routingStrategy");
        var routes = (List<Map<String, Object>>) action.get("routes");

        switch (routingStrategy.toLowerCase()) {
            case "percentage":
                executePercentageStrategy(context, routes);
                break;
            case "flat":
                executeFlatStrategy(context, routes);
                break;
            default:
                throw new RuntimeException("Unknown routing strategy: " + routingStrategy);
        }
    }

    private void executePercentageStrategy(PaymentContext context, List<Map<String, Object>> routes) {
        double randomValue = random.nextDouble() * 100;
        double cumulativePercentage = 0;

        for (var route : routes) {
            String acquirer = (String) route.get("acquirer");
            double percentage = ((Number) route.get("percentage")).doubleValue();
            cumulativePercentage += percentage;

            if (randomValue <= cumulativePercentage) {
                context.setPaymentRoute(acquirer);
                return;
            }
        }

        throw new RuntimeException("No acquirer selected.");
    }

    private void executeFlatStrategy(PaymentContext context, List<Map<String, Object>> routes) {
        double amount = context.getAmount();

        for (var route : routes) {
            String acquirer = (String) route.get("acquirer");
            double min = ((Number) route.get("min")).doubleValue();
            Object maxObj = route.get("max");
            double max = maxObj == null ? Double.MAX_VALUE : ((Number) maxObj).doubleValue();

            if (amount >= min && amount <= max) {
                context.setPaymentRoute(acquirer);
                return;
            }
        }

        throw new RuntimeException("No acquirer selected.");
    }
}
