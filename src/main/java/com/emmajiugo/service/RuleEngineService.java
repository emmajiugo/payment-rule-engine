package com.emmajiugo.service;

import com.emmajiugo.dao.CustomerDao;
import com.emmajiugo.dao.RuleDao;
import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.entity.Rule;
import com.emmajiugo.ruleImpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleEngineService {

    private final RuleDao ruleDao;
    private final CustomerDao customerDao;

    @Autowired
    public RuleEngineService(RuleDao ruleDao, CustomerDao customerDao) {
        this.ruleDao = ruleDao;
        this.customerDao = customerDao;
    }

    public ResponseEntity<PaymentContext> processPayment(PaymentContext context) throws Exception {
        List<PaymentRule> paymentRules = new ArrayList<>();

        var rules = ruleDao.getRules();

        for (Rule rule : rules) {
            String ruleName = rule.getName();

            switch (ruleName) {
                case "PaymentMethod"    -> paymentRules.add(new PaymentMethodRule(rule.getMeta()));
                case "TransactionRoute" -> paymentRules.add(new TransactionRouteRule(rule.getMeta()));
                case "Require3DS"       -> paymentRules.add(new Required3DSRule(rule.getMeta(), customerDao));
                case "HighRiskCountry"  -> paymentRules.add(new HighRiskCountryRule(rule.getMeta()));
                case "FeeStructure"     -> paymentRules.add(new FeeRule(rule.getMeta()));
                case "EmployeeFeature"  -> paymentRules.add(new EmployeeFeatureRule(rule.getMeta(), customerDao));
                default -> throw new RuntimeException("No implementation found for rule: " + ruleName);
            }
        }

        paymentRules.forEach(paymentRule -> {
            if (paymentRule.evaluate(context)) {
                paymentRule.execute(context);
            }
        });

        return ResponseEntity.ok(context);
    }
}
