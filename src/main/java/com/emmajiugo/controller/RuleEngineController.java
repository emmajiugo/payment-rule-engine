package com.emmajiugo.controller;

import com.emmajiugo.dto.ErrorMapping;
import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.dto.PaymentRequest;
import com.emmajiugo.service.RuleEngineService;
import com.emmajiugo.utils.Utils;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        try {
            log.info("Evaluating payment: {}", paymentRequest);

            var paymentContext = Utils.fromJson(Utils.toJson(paymentRequest), PaymentContext.class);

            return ResponseEntity.ok(ruleEngineService.processPayment(paymentContext));

        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorMapping("INTERNAL ERROR", "Error processing payment: " + e.getMessage(), null));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() throws Exception {
        return ResponseEntity.ok("Connection established!");
    }
}
