package com.example.statementgenerator.client;

import org.springframework.stereotype.Component;

@Component
public class CoreBankingSystemClient {

    public String fetchStatementData(String accountNumber) throws Exception {
        // Simulate fetching data from the core banking system.
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new Exception("Invalid account number.");
        }

        return "Mock statement data for account number: " + accountNumber;
    }
}
