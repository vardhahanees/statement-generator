package com.example.statementgenerator.controller;

import com.example.statementgenerator.model.StatementEntity;
import com.example.statementgenerator.service.StatementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class StatementRequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StatementService statementService;

    @InjectMocks
    private StatementRequestController statementRequestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(statementRequestController).build();
    }

    @Test
    public void testRequestStatement_Success() throws Exception {
        // Mock the service to return a successful response
        when(statementService.initiateStatementGeneration(any(StatementEntity.class)))
                .thenReturn(CompletableFuture.completedFuture("1"));

        // Prepare the JSON request body
        String requestBody = "{ \"accountNumber\": \"123456789\", \"fromDate\": \"2024-01-01\", \"toDate\": \"2024-12-31\" }";

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/v1/statements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print());

        // Verify that the service was called once
        verify(statementService, times(1)).initiateStatementGeneration(any(StatementEntity.class));
    }

    @Test
    public void testRequestStatement_Failure() throws Exception {
        // Mock the service to return a failed future
        when(statementService.initiateStatementGeneration(any(StatementEntity.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Core banking error")));

        // Prepare the JSON request body
        String requestBody = "{ \"accountNumber\": \"123456789\", \"fromDate\": \"2024-01-01\", \"toDate\": \"2024-12-31\" }";

        // Perform the POST request and verify the error response
        mockMvc.perform(post("/api/v1/statements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print());

        // Verify that the service was called once
        verify(statementService, times(1)).initiateStatementGeneration(any(StatementEntity.class));
    }
}
