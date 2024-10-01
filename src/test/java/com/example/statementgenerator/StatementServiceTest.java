package com.example.statementgenerator.service;

import com.example.statementgenerator.client.CoreBankingSystemClient;
import com.example.statementgenerator.model.StatementEntity;
import com.example.statementgenerator.repository.StatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StatementServiceTest {

    @Mock
    private CoreBankingSystemClient coreBankingSystemClient;

    @Mock
    private StatementRepository statementRepository;

    @InjectMocks
    private StatementService statementService;

    private StatementEntity mockRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Prepare mock request
        mockRequest = new StatementEntity();
        mockRequest.setAccountNumber("123456789");
        mockRequest.setFromDate(LocalDate.parse("2024-01-01"));
        mockRequest.setToDate(LocalDate.parse("2024-12-31"));
        mockRequest.setStatus("NEW");
    }

    @Test
    public void testInitiateStatementGeneration_Success() throws Exception {
        // Mock successful response from CoreBankingSystemClient
        when(coreBankingSystemClient.fetchStatementData(mockRequest.getAccountNumber())).thenReturn("statementData");

        // Mock the save method to return the entity with an ID
        StatementEntity savedEntity = new StatementEntity();
        savedEntity.setId(1L); // Assign an ID
        savedEntity.setAccountNumber(mockRequest.getAccountNumber());
        savedEntity.setFromDate(mockRequest.getFromDate());
        savedEntity.setToDate(mockRequest.getToDate());
        savedEntity.setStatus("PROCESSING");

        when(statementRepository.save(any(StatementEntity.class))).thenReturn(savedEntity);

        // Call the method under test
        CompletableFuture<String> future = statementService.initiateStatementGeneration(mockRequest);

        // Wait for the future to complete and assert the result
        String resultId = future.get();
        assertNotNull(resultId);

        // Verify that the statement was saved twice (once for processing and once for completion)
        ArgumentCaptor<StatementEntity> statementCaptor = ArgumentCaptor.forClass(StatementEntity.class);
        verify(statementRepository, times(2)).save(statementCaptor.capture());

        // Verify status change to "PROCESSING" and then "COMPLETED"
        StatementEntity completedStatement = statementCaptor.getAllValues().get(1);
        assertEquals("COMPLETED", completedStatement.getStatus());
        assertNotNull(completedStatement.getPdfUrl());
    }

    @Test
    public void testInitiateStatementGeneration_Failure() throws Exception {
        // Mock an exception when fetching statement data
        when(coreBankingSystemClient.fetchStatementData(mockRequest.getAccountNumber())).thenThrow(new RuntimeException("Core banking error"));

        // Mock the save method to return a statement with an ID
        StatementEntity savedEntity = new StatementEntity();
        savedEntity.setId(1L); // Assign an ID
        savedEntity.setAccountNumber(mockRequest.getAccountNumber());
        savedEntity.setFromDate(mockRequest.getFromDate());
        savedEntity.setToDate(mockRequest.getToDate());
        savedEntity.setStatus("PROCESSING");

        when(statementRepository.save(any(StatementEntity.class))).thenReturn(savedEntity);

        // Call the method under test and expect a failure
        CompletableFuture<String> future = statementService.initiateStatementGeneration(mockRequest);
        assertTrue(future.isCompletedExceptionally());

        // Verify that the statement was saved twice (once for processing and once for failure)
        ArgumentCaptor<StatementEntity> statementCaptor = ArgumentCaptor.forClass(StatementEntity.class);
        verify(statementRepository, times(2)).save(statementCaptor.capture());

        // Verify status change to "PROCESSING" and then "FAILED"
        StatementEntity failedStatement = statementCaptor.getAllValues().get(1);
        assertEquals("FAILED", failedStatement.getStatus());

        // Verify that no PDF URL was generated
        assertNull(failedStatement.getPdfUrl());
    }
}
