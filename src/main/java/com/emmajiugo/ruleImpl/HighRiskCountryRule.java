package com.emmajiugo.ruleImpl;

import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.ruleImpl.mapper.HighRiskCountryMapper;
import com.emmajiugo.utils.Utils;

public class HighRiskCountryRule implements PaymentRule {
    private final HighRiskCountryMapper ruleMapper;

    public HighRiskCountryRule(String ruleMeta) {
        this.ruleMapper = Utils.fromJson(ruleMeta, HighRiskCountryMapper.class);
    }

    @Override
    public boolean evaluate(PaymentContext context) {

        return ruleMapper.getCondition().getCountries()
                .stream().anyMatch(country -> {
                    if (country.getName().equalsIgnoreCase(context.getCountry())) {
                        return context.getAmount() > country.getThreshold().getAmount()
                                && context.getCurrency().equalsIgnoreCase(country.getThreshold().getCurrency());
                    }
                    return false;
                });
    }

    @Override
    public void execute(PaymentContext context) {
        context.setRequiredVerification(ruleMapper.getAction().isRequireVerification());
    }
}
