package com.emmajiugo.ruleImpl;

import com.emmajiugo.dao.CustomerDao;
import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.entity.Customer;
import com.emmajiugo.ruleImpl.mapper.EmployeeFeatureMapper;
import com.emmajiugo.ruleImpl.mapper.EmployeeFeatureMapper.Action;
import com.emmajiugo.utils.Utils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class EmployeeFeatureRule implements PaymentRule {
    private final List<EmployeeFeatureMapper> ruleMapper;
    private final CustomerDao customerDao;
    private Action action;

    public EmployeeFeatureRule(String ruleMeta, CustomerDao customerDao) {
        Type listType = new TypeToken<List<EmployeeFeatureMapper>>() {}.getType();
        this.ruleMapper = Utils.fromJsonArray(ruleMeta, listType);
        this.customerDao = customerDao;
    }

    @Override
    public boolean evaluate(PaymentContext context) {
        Customer customer = getCustomer(context.getCustomerEmail());

        for (EmployeeFeatureMapper rule : ruleMapper) {
            String requiredRole = rule.getCondition().getRequiredRole();

            if (requiredRole.equalsIgnoreCase(customer.getRole())) {
                this.action = rule.getAction();
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(PaymentContext context) {
        List<String> enabledFeatures = action.getFeatures().stream()
                .filter(EmployeeFeatureMapper.Feature::isEnabled)
                .map(EmployeeFeatureMapper.Feature::getName)
                .toList();

        context.setEnabledFeatures(enabledFeatures);
        context.setEmployee(true);
    }

    private Customer getCustomer(String email) {
        return customerDao.getCustomerByEmail(email);
    }
}
