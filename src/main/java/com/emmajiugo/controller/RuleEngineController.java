package com.emmajiugo.controller;

import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.service.RuleEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-rule")
public class RuleEngineController {

    private final RuleEngineService ruleEngineService;

    @Autowired
    public RuleEngineController(RuleEngineService ruleEngineService) {
        this.ruleEngineService = ruleEngineService;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentContext> processPayment(
            @RequestBody PaymentContext paymentContext
    ) throws Exception {
        return ruleEngineService.processPayment(paymentContext);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() throws Exception {
        return ResponseEntity.ok("Connection established!");
    }
}
