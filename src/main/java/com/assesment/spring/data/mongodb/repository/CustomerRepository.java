package com.assesment.spring.data.mongodb.repository;

import com.assesment.spring.data.mongodb.model.CustomerEntity;
import com.assesment.spring.data.mongodb.model.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<CustomerEntity, String> {
	Page<CustomerEntity> findByAccountId(String accountId, Pageable pageable);
}
