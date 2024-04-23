package com.Bank.Manager.business.service.impl;

import com.Bank.Manager.business.mapper.StatementMapStructMapper;
import com.Bank.Manager.business.repository.StatementRepository;
import com.Bank.Manager.business.repository.model.StatementDAO;
import com.Bank.Manager.business.service.StatementService;
import com.Bank.Manager.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
    public void saveStatements(List<Statement> statements) {
        List<StatementDAO> statementDAOs = statements.stream()
                .map(statementMapStructMapper::statementToStatementDAO)
                .collect(Collectors.toList());

        statementRepository.saveAll(statementDAOs);
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
