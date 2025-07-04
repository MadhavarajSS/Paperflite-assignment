package com.assesment.spring.data.mongodb;

import com.assesment.spring.data.mongodb.service.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootDataMongodbApplication implements CommandLineRunner {

    @Autowired
    private MigrationService migrationService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataMongodbApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        migrationService.deleteCustomersWithNoAccount();
    }
}
