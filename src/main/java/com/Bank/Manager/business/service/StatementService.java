package com.Bank.Manager.business.service;

import com.Bank.Manager.model.Statement;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

public interface StatementService {
    List<Statement> findAllStatements();


    void saveStatementsFromMultipleFiles(List<MultipartFile> files) throws Exception;

    List<Statement> findStatementsByDateRange(LocalDateTime dateFrom, LocalDateTime dateTo);

    Long getAccountBalance(Long accountNumber);

    Long getAccountBalanceByDateRange(Long accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo);

}
