package com.emmajiugo.dao;

import com.emmajiugo.entity.Rule;
import com.emmajiugo.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleDao {
    private final RuleRepository ruleRepository;

    @Autowired
    public RuleDao(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public List<Rule> getRules() {
        return ruleRepository.findAll();
    }

}
