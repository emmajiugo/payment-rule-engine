package com.emmajiugo.dao;

import com.emmajiugo.cache.CacheBean;
import com.emmajiugo.cache.CacheStore;
import com.emmajiugo.entity.Customer;
import com.emmajiugo.repository.CustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CustomerDao {
    private final CustomerRepository customerRepository;
    private final CacheStore<Customer> customerCache;

    @Autowired
    public CustomerDao(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.customerCache = CacheBean.customerCache();
    }

    public Customer getCustomerByEmail(String email) {
        var cachedCustomer = customerCache.get(email);

        if (cachedCustomer != null) {
            log.info("Fetching customer from cache");
            return cachedCustomer;
        }

        var customer = customerRepository.findByEmail(email)
                .orElse(null);

        if (customer != null) {
            log.info("Fetching customer from database and adding to cache");
            customerCache.add(email, customer);
        }

        return customer;
    }

}
