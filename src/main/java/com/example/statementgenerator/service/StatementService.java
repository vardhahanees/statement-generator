package com.example.statementgenerator.service;

import com.example.statementgenerator.client.CoreBankingSystemClient;
import com.example.statementgenerator.model.StatementEntity;
import com.example.statementgenerator.repository.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class StatementService {

    @Autowired
    private CoreBankingSystemClient coreBankingSystemClient;

    @Autowired
    private StatementRepository statementRepository;

    @Async
    public CompletableFuture<String> initiateStatementGeneration(StatementEntity request) {
        // Save the initial request in the DB
        StatementEntity statement = new StatementEntity();
        statement.setAccountNumber(request.getAccountNumber());
        statement.setFromDate(request.getFromDate());
        statement.setToDate(request.getToDate());
        statement.setStatus("PROCESSING");

        // Save and then retrieve the saved entity to get the generated ID
        statement = statementRepository.save(statement);

        try {
            // Fetch statement data and generate the PDF
            String statementData = coreBankingSystemClient.fetchStatementData(request.getAccountNumber());
            String pdfUrl = generatePdf(statementData);

            // Update with the final status and PDF URL
            statement.setStatus("COMPLETED");
            statement.setPdfUrl(pdfUrl);
            statementRepository.save(statement);

            // Simulate notifying the customer
            notifyCustomer(statement.getId(), pdfUrl);
        } catch (Exception e) {
            statement.setStatus("FAILED");
            statementRepository.save(statement);
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);
        }

        return CompletableFuture.completedFuture(statement.getId().toString());
    }

    private String generatePdf(String statementData) {
        return "http://localhost:8080/api/v1/statements/download/" + UUID.randomUUID().toString() + ".pdf";
    }

    private void notifyCustomer(Long statementId, String pdfUrl) {
        System.out.println("Notification sent: Your statement is ready. Download at: " + pdfUrl);
    }
}
