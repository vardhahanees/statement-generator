package com.example.statementgenerator.controller;

import com.example.statementgenerator.model.StatementEntity;
import com.example.statementgenerator.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/statements")
public class StatementRequestController {

    private final StatementService statementService;

    @Autowired
    public StatementRequestController(StatementService statementService) {
        this.statementService = statementService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<String>> requestStatement(@RequestBody StatementEntity request) {
        return statementService.initiateStatementGeneration(request)
                .thenApply(requestId -> ResponseEntity.accepted().body("Request accepted with ID: " + requestId))
                .exceptionally(ex -> ResponseEntity.status(500).body("Error: " + ex.getMessage()));
    }

}
