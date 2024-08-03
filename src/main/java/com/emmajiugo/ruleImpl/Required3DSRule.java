package com.emmajiugo.ruleImpl;

import com.emmajiugo.dao.CustomerDao;
import com.emmajiugo.dto.PaymentContext;
import com.emmajiugo.dto.RuleDto;
import com.emmajiugo.entity.Customer;
import com.emmajiugo.utils.DateHelper;
import com.emmajiugo.utils.Utils;

import java.time.LocalDateTime;
import java.util.Map;

public class Required3DSRule implements PaymentRule {
    private final RuleDto ruleDto;
    private final CustomerDao customerDao;
    private Boolean required3DS = true; ;

    public Required3DSRule(String ruleMeta, CustomerDao customerDao) {
        this.ruleDto = Utils.fromJson(ruleMeta, RuleDto.class);
        this.customerDao = customerDao;
    }

    @Override
    public boolean evaluate(PaymentContext context) {
        var condition = Utils.fromJson(Utils.toJson(ruleDto.condition()), Map.class);
        var action = Utils.fromJson(Utils.toJson(ruleDto.action()), Map.class);

        if (condition.get("customerType").toString().equals("existing")) {
            Customer customer = customerDao.getCustomerByEmail(context.getCustomerEmail());

            if (customer == null) return false;
            if (customer.getLast3DSDate() == null) return false;

            var checkPeriod = (Map) condition.get("checkPeriod");

            long lastSuccessful3DS = switch ((String) checkPeriod.get("unit")) {
                case "DAYS" -> DateHelper.diffInDays(customer.getLast3DSDate());
                case "MONTHS" -> DateHelper.diffInMonths(customer.getLast3DSDate());
                case "YEARS" -> DateHelper.diffInYears(customer.getLast3DSDate());
                default -> throw new RuntimeException("Invalid last3DS value");
            };

            System.out.println("Last 3DS in days: " + lastSuccessful3DS);
            long checkPeriodValue = ((Number) checkPeriod.get("value")).longValue();

            if (lastSuccessful3DS < checkPeriodValue) {
                required3DS = ! (Boolean) action.get("waive3DS");
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
