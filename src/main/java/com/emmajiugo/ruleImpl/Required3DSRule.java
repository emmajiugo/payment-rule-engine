package com.emmajiugo.ruleImpl;

import com.emmajiugo.dao.CustomerDao;
import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.entity.Customer;
import com.emmajiugo.ruleImpl.mapper.Required3DSMapper;
import com.emmajiugo.utils.DateHelper;
import com.emmajiugo.utils.Utils;


public class Required3DSRule implements PaymentRule {
    private final Required3DSMapper ruleMapper;
    private final CustomerDao customerDao;
    private boolean required3DS;

    public Required3DSRule(String ruleMeta, CustomerDao customerDao) {
        this.ruleMapper = Utils.fromJson(ruleMeta, Required3DSMapper.class);
        this.customerDao = customerDao;
    }

    @Override
    public boolean evaluate(PaymentContext context) {
        var condition = ruleMapper.getCondition();
        var action = ruleMapper.getAction();

        if (condition.getCustomerType().equals("existing")) {
            Customer customer = customerDao.getCustomerByEmail(context.getCustomerEmail());

            if (customer == null) return false;
            if (customer.getLast3DSDate() == null) return false;

            var checkPeriod = condition.getCheckPeriod();

            long lastSuccessful3DS = switch (checkPeriod.getUnit()) {
                case "DAYS" -> DateHelper.diffInDays(customer.getLast3DSDate());
                case "MONTHS" -> DateHelper.diffInMonths(customer.getLast3DSDate());
                case "YEARS" -> DateHelper.diffInYears(customer.getLast3DSDate());
                default -> throw new RuntimeException("Invalid last3DS value");
            };

            if (lastSuccessful3DS < checkPeriod.getValue()) {
                required3DS = ! action.isWaive3DS();
                return true;
            }
        }

        return false;
    }

    @Override
    public void execute(PaymentContext context) {
        context.setRequired3DS(required3DS);
    }

}
