package com.assesment.spring.data.mongodb.controller;

import com.assesment.spring.data.mongodb.model.CustomerEntity;
import com.assesment.spring.data.mongodb.model.AccountEntity;
import com.assesment.spring.data.mongodb.repository.AccountRepository;
import com.assesment.spring.data.mongodb.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping
    public CustomerEntity createCustomer(@RequestBody CustomerEntity customer) {
        logger.info("Creating new customer: {} {}", customer.getFirstName(), customer.getLastName());

        if (customer.getAccountId() != null) {
            Optional<AccountEntity> account = accountRepository.findById(customer.getAccountId().getId());
            if (account.isPresent()) {
                customer.setAccountId(account.get());
            } else {
                logger.warn("Provided account ID does not exist. Setting account to null.");
                customer.setAccountId(null);
            }
        }

        CustomerEntity savedCustomer = customerRepository.save(customer);
        logger.info("Customer created with ID: {}", savedCustomer.getId());

        return savedCustomer;
    }

    @PutMapping("/{id}")
    public CustomerEntity updateCustomer(@PathVariable String id, @RequestBody CustomerEntity customerUpdate) {
        logger.info("Updating customer with ID: {}", id);

        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            CustomerEntity existingCustomer = optionalCustomer.get();
            existingCustomer.setFirstName(customerUpdate.getFirstName());
            existingCustomer.setLastName(customerUpdate.getLastName());

            if (customerUpdate.getAccountId() != null) {
                Optional<AccountEntity> account = accountRepository.findById(customerUpdate.getAccountId().getId());
                if (account.isPresent()) {
                    existingCustomer.setAccountId(account.get());
                } else {
                    logger.warn("Provided account ID for update does not exist. Keeping old account.");
                }
            }

            CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);
            logger.info("Customer updated successfully for ID: {}", id);

            return updatedCustomer;
        } else {
            logger.error("Customer not found with ID: {}", id);
            throw new RuntimeException("Customer not found");
        }
    }

    @GetMapping("/account/{accountId}")
    public Page<CustomerEntity> getCustomersByAccountId(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        logger.info("Fetching customers for account ID: {}, page: {}, limit: {}", accountId, page, limit);

        Optional<AccountEntity> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            Pageable pageable = PageRequest.of(page, limit);
            Page<CustomerEntity> customersPage = customerRepository.findByAccountId(account.get(), pageable);
            logger.info("Found {} customers for account ID: {}", customersPage.getTotalElements(), accountId);
            return customersPage;
        } else {
            logger.error("Account not found with ID: {}", accountId);
            throw new RuntimeException("Account not found");
        }
    }
}
