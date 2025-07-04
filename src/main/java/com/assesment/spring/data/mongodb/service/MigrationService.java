package com.assesment.spring.data.mongodb.service;

import com.assesment.spring.data.mongodb.model.CustomerEntity;
import com.assesment.spring.data.mongodb.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MigrationService {

    private static final Logger logger = LoggerFactory.getLogger(MigrationService.class);

    @Autowired
    private CustomerRepository customerRepository;

    public void deleteCustomersWithNoAccount() {
        int page = 0;
        int size = 10; 

        Page<CustomerEntity> customerPage;

        do {
            customerPage = customerRepository.findAll(PageRequest.of(page, size));
            
            for (CustomerEntity customer : customerPage.getContent()) {
                if (customer.getAccountId() == null) {
                    logger.info("Deleting customer with ID: {}", customer.getId());
                    customerRepository.delete(customer);
                } else {
                    logger.info("Customer with ID: {} has an account. Skipping.", customer.getId());
                }
            }

            page++;

        } while (customerPage.hasNext());

        logger.info("Customer cleanup migration completed.");
    }
}