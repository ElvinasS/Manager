package com.Bank.Manager.business.service;

import com.Bank.Manager.model.Statement;

import java.util.List;

public interface StatementService {
    List<Statement> findAllStatements();
}
