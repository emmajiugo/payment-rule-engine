package com.emmajiugo.ruleImpl;

import com.emmajiugo.dto.PaymentContext;

public interface PaymentRule {
    boolean evaluate(PaymentContext context);
    void execute(PaymentContext context);
}
