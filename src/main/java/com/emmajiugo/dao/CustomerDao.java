package com.emmajiugo.dao;

import com.emmajiugo.entity.Customer;
import com.emmajiugo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerDao {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerDao(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElse(null);
    }

}
