package com.example.statementgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Enable async processing
public class StatementGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatementGeneratorApplication.class, args);
    }
}
