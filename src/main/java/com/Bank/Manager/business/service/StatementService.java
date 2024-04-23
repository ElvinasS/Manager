package com.Bank.Manager.business.service;

import com.Bank.Manager.model.Statement;

import java.util.List;

public interface StatementService {
    List<Statement> findAllStatements();

    Statement saveStatement(Statement statement) throws Exception;

    void saveStatements(List<Statement> statements);
}
