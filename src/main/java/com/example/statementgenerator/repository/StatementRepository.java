package com.example.statementgenerator.repository;

import com.example.statementgenerator.model.StatementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepository extends JpaRepository<StatementEntity, Long> {
}