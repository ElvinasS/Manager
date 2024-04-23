package com.Bank.Manager.business.service.impl;

import com.Bank.Manager.business.mapper.StatementMapStructMapper;
import com.Bank.Manager.business.repository.StatementRepository;
import com.Bank.Manager.business.repository.model.StatementDAO;
import com.Bank.Manager.business.service.StatementService;
import com.Bank.Manager.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
