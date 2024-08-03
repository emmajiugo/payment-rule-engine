package com.emmajiugo.ruleImpl;

import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.ruleImpl.mapper.FeeRuleMapper;
import com.emmajiugo.ruleImpl.mapper.FeeRuleMapper.Action;
import com.emmajiugo.utils.Utils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class FeeRule implements PaymentRule {
    private final List<FeeRuleMapper> ruleMapper;
    private Action action;

    public FeeRule(String ruleMeta) {
        Type listType = new TypeToken<List<FeeRuleMapper>>() {}.getType();
        this.ruleMapper = Utils.fromJsonArray(ruleMeta, listType);
    }

    @Override
    public boolean evaluate(PaymentContext context) {
        for (FeeRuleMapper rule : ruleMapper) {
            String cardType = rule.getCondition().getCardType();
            String currency = rule.getCondition().getCurrency();
            double maxAmount = rule.getCondition().getMaxAmount();

            if (cardType.equalsIgnoreCase(context.getCardType())
                    && currency.equalsIgnoreCase(context.getCurrency())
                    && maxAmount > context.getAmount()) {
                this.action = rule.getAction();
                return true;
            }
        }

        return false;
    }

    @Override
    public void execute(PaymentContext context) {
        double fee = 0;

        if ("percentage".equalsIgnoreCase(action.getFeeType())) {
            fee = context.getAmount() * (action.getFeeValue() / 100.0);
        } else if ("flat".equalsIgnoreCase(action.getFeeType())) {
            fee = action.getFeeValue();
        }
        
        context.setFee(fee);
    }
}
