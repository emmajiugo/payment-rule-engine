package com.emmajiugo.config;

import com.emmajiugo.entity.Customer;
import com.emmajiugo.entity.Rule;
import com.emmajiugo.repository.CustomerRepository;
import com.emmajiugo.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Not meant for production
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final RuleRepository ruleRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public DataSeeder(RuleRepository ruleRepository, CustomerRepository customerRepository) {
        this.ruleRepository = ruleRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedCustomers();
        seedRules();
    }

    private void seedCustomers() {
        Customer customer1 = new Customer("e@x.com", "password1", "EMPLOYEE", "male", "Nigeria");
        customer1.setLast3DSDate(LocalDateTime.now().minusDays(2));

        Customer customer2 = new Customer("xyz@x.com", "password2", "USER", "female", "Norway");
        customer2.setLast3DSDate(LocalDateTime.now().minusDays(370));

        // Check if customer1 exists
        if (customerRepository.findByEmail(customer1.getEmail()).isEmpty()) {
            customerRepository.save(customer1);
            System.out.println("Customer seeded: " + customer1.getEmail());
        } else {
            System.out.println("Customer already exists: " + customer1.getEmail());
        }

        // Check if customer2 exists
        if (customerRepository.findByEmail(customer2.getEmail()).isEmpty()) {
            customerRepository.save(customer2);
            System.out.println("Customer seeded: " + customer2.getEmail());
        } else {
            System.out.println("Customer already exists: " + customer2.getEmail());
        }
    }

    private void seedRules() throws Exception {
        List<Rule> rulesToSeed = Arrays.asList(
                createRule(
                        "PaymentMethod",
                        loadSeederFile("/seeder/payment-method-rule.json")
                ),
                createRule(
                        "TransactionRoute",
                        loadSeederFile("/seeder/transaction-route-rule.json")
                ),
                createRule(
                        "Require3DS",
                        loadSeederFile("/seeder/3ds-waiver-rule.json")
                ),
                createRule(
                        "HighRiskCountry",
                        loadSeederFile("/seeder/highrisk-country-rule.json")
                ),
                createRule(
                        "FeeStructure",
                        loadSeederFile("/seeder/fee-rule.json")
                ),
                createRule(
                        "EmployeeFeature",
                        loadSeederFile("/seeder/employee-feature-rule.json")
                )
        );

        for (Rule rule : rulesToSeed) {
            if (ruleRepository.findByName(rule.getName()).isEmpty()) {
                ruleRepository.save(rule);
                System.out.println("Seeded rule: " + rule.getName());
            } else {
                System.out.println("Rule already exists: " + rule.getName());
            }
        }
    }

    private Rule createRule(String name, String metaJson) throws Exception {
        Rule rule = new Rule();
        rule.setName(name);
        rule.setMeta(metaJson);
        return rule;
    }

    private String loadSeederFile(String filePath) throws Exception {
        InputStream inputStream = DataSeeder.class.getResourceAsStream(filePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
