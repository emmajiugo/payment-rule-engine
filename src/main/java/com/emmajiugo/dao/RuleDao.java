package com.emmajiugo.dao;

import com.emmajiugo.cache.CacheBean;
import com.emmajiugo.cache.CacheStore;
import com.emmajiugo.entity.Rule;
import com.emmajiugo.repository.RuleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class RuleDao {
    private final RuleRepository ruleRepository;
    private final CacheStore<List<Rule>> ruleCache;

    @Autowired
    public RuleDao(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
        this.ruleCache = CacheBean.ruleCache();
    }

    public List<Rule> getRules() {
        String key = "allRules";

        var cachedRules = ruleCache.get(key);

        if (cachedRules != null) {
            log.info("Fetching rules from cache");
            return cachedRules;
        }

        log.info("Fetching rules from database and adding to cache");
        var rules = ruleRepository.findAll();
        ruleCache.add(key, rules);

        return rules;
    }

}
