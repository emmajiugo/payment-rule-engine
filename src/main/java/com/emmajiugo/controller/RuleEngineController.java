package com.emmajiugo.controller;

import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.service.RuleEngineService;
import com.emmajiugo.utils.PaymentContextValidator;
import com.emmajiugo.utils.PaymentContextValidator.Error;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/payment-rule")
public class RuleEngineController {

    private final RuleEngineService ruleEngineService;

    @Autowired
    public RuleEngineController(RuleEngineService ruleEngineService) {
        this.ruleEngineService = ruleEngineService;
    }

    @PostMapping("/process")
    public ResponseEntity<Object> processPayment(@RequestBody PaymentContext paymentContext) {
        try {
            log.info("Evaluating payment: {}", paymentContext);

            //simple validation
            var error = PaymentContextValidator.validate(paymentContext);

            if (error != null) {
                log.error("Validation failed: {}", error);
                return ResponseEntity.badRequest().body(error);
            }

            return ResponseEntity.ok(ruleEngineService.processPayment(paymentContext));

        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new Error("INTERNAL ERROR", "Error processing payment: " + e.getMessage(), null));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() throws Exception {
        return ResponseEntity.ok("Connection established!");
    }
}
