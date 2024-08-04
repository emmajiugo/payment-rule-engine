package com.emmajiugo.ruleImpl;

import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.ruleImpl.mapper.TransactionRouteMapper;
import com.emmajiugo.ruleImpl.mapper.TransactionRouteMapper.Route;
import com.emmajiugo.utils.Utils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class TransactionRouteRule implements PaymentRule {
    private final List<TransactionRouteMapper> ruleMapper;
    private final Random random = new Random();
    private TransactionRouteMapper matchedRule = null;

    public TransactionRouteRule(String ruleMeta) {
        Type listType = new TypeToken<List<TransactionRouteMapper>>() {}.getType();
        this.ruleMapper = Utils.fromJsonArray(ruleMeta, listType);
    }

    @Override
    public boolean evaluate(PaymentContext context) {
        for (var rule : ruleMapper) {
            String cardScheme = rule.getCondition().getCardScheme();
            String currency = rule.getCondition().getCurrency();

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

        String routingStrategy = matchedRule.getAction().getRoutingStrategy();
        var routes =  matchedRule.getAction().getRoutes();

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

    private void executePercentageStrategy(PaymentContext context, List<Route> routes) {
        double randomValue = random.nextDouble() * 100;
        double cumulativePercentage = 0;

        for (var route : routes) {
            String acquirer = route.getAcquirer();
            double percentage = route.getPercentage();
            cumulativePercentage += percentage;

            if (randomValue <= cumulativePercentage) {
                context.setPaymentRoute(acquirer);
                return;
            }
        }

        throw new RuntimeException("No acquirer selected.");
    }

    private void executeFlatStrategy(PaymentContext context, List<Route> routes) {
        double amount = context.getAmount();

        for (var route : routes) {
            String acquirer = route.getAcquirer();
            double minAmount = route.getMin();
            double maxAmount = route.getMax();
            double max = maxAmount == 0.0 ? Double.MAX_VALUE : maxAmount;

            if (amount >= minAmount && amount <= max) {
                context.setPaymentRoute(acquirer);
                return;
            }
        }

        throw new RuntimeException("No acquirer selected.");
    }
}
