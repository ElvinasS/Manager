package com.Bank.Manager.business.service.impl;

import com.Bank.Manager.business.mapper.StatementMapStructMapper;
import com.Bank.Manager.business.repository.StatementRepository;
import com.Bank.Manager.business.repository.model.StatementDAO;
import com.Bank.Manager.business.service.StatementService;
import com.Bank.Manager.business.utils.CsvUtils;
import com.Bank.Manager.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatementServiceImpl implements StatementService {

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    StatementMapStructMapper statementMapStructMapper;


    @Override
    public List<Statement> findAllStatements() {
        List<StatementDAO> statementDAOList = statementRepository.findAll();
        return statementDAOList.stream().map(statementMapStructMapper::statementDAOToStatement).collect(Collectors.toList());
    }

    @Override
    public Statement saveStatement(Statement statement) throws Exception {
        if (!hasMatch(statement)) {
            StatementDAO statementSaved = statementRepository.save(statementMapStructMapper.statementToStatementDAO(statement));
            return statementMapStructMapper.statementDAOToStatement(statementSaved);
        }else {
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
    }

    @Override
    public void saveStatementsFromMultipleFiles(List<MultipartFile> files) throws Exception {
        List<Statement> allStatements = new ArrayList<>();

        for (MultipartFile file : files) {
            List<Statement> statements = CsvUtils.parseCsvFile(file.getInputStream());
            allStatements.addAll(statements);
        }

        saveAllStatements(allStatements);
    }

    private void saveAllStatements(List<Statement> statements) {
        List<StatementDAO> statementDAOs = statements.stream()
                .map(statementMapStructMapper::statementToStatementDAO)
                .collect(Collectors.toList());

        statementRepository.saveAll(statementDAOs);
    }

    @Override
    public List<Statement> findStatementsByDateRange(LocalDateTime dateFrom, LocalDateTime dateTo) {
        List<StatementDAO> statementDAOList = statementRepository.findByDateRange(dateFrom, dateTo);
        return statementDAOList.stream().map(statementMapStructMapper::statementDAOToStatement).collect(Collectors.toList());
    }

    public boolean hasMatch(Statement statement){
        return statementRepository.findAll().stream().anyMatch(statementDAO -> isSame(statement, statementDAO));
    }

    private boolean isSame(Statement statement, StatementDAO statementDAO) {
        return statementDAO.getAccountNumber().equals(statement.getAccountNumber()) &&
                statementDAO.getBeneficiaryNumber().equals(statement.getBeneficiaryNumber()) &&
                statementDAO.getAmount().equals(statement.getAmount());
    }
}
