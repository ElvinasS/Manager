package com.Bank.Manager.business.service.impl;

import com.Bank.Manager.business.mapper.StatementMapStructMapper;
import com.Bank.Manager.business.repository.StatementRepository;
import com.Bank.Manager.business.repository.model.StatementDAO;
import com.Bank.Manager.business.utils.CsvUtils;
import com.Bank.Manager.model.Statement;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StatementServiceImplTest {

    @Mock
    private StatementRepository repository;

    @InjectMocks
    private StatementServiceImpl service;

    @Mock
    private StatementMapStructMapper mapper;

    @Mock
    private CsvUtils csvUtils;


    private Statement statement;
    private StatementDAO statementDAO;
    private List<Statement> statementList;
    private List<StatementDAO> statementDAOList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllStatement_successful(){
        statementDAOList = createStatementDAOList();
        statementList = createStatementList();

        when(repository.findAll()).thenReturn(statementDAOList);
        List<Statement> statements = service.findAllStatements();
        assertEquals(2, statements.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findAllStatement_invalid(){
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(service.findAllStatements().isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void saveStatementFromMultipleFiles_successful() throws Exception {
        // Create mock MultipartFile instances with actual content
        MultipartFile file1 = createMockMultipartFile("file1.csv", "account_number,operation_date,beneficiary_number,comment,amount,currency\n1234,2024-01-01T10:00:00,5678,test,100,test");
        MultipartFile file2 = createMockMultipartFile("file2.csv", "account_number,operation_date,beneficiary_number,comment,amount,currency\n5678,2024-01-02T10:00:00,1234,test,200,test");

        service.saveStatementsFromMultipleFiles(Arrays.asList(file1, file2));

        verify(repository).saveAll(anyList());
    }

    @Test
    void getAccountBalanceByDateRange_successful() {
        Long accountNumber = 12345L;
        LocalDateTime dateFrom = LocalDateTime.parse("2014-01-01T00:00:00");
        LocalDateTime dateTo = LocalDateTime.parse("2014-01-10T00:00:00");

        List<StatementDAO> statements = Arrays.asList(
                createStatementDAOWithDate(accountNumber, "2014-01-05T00:00:00"),
                createStatementDAOWithDate(accountNumber, "2014-01-07T00:00:00")
        );

        when(repository.findByAccountNumberAndDateRange(accountNumber, dateFrom, dateTo)).thenReturn(statements);
        Long balance = service.getAccountBalanceByDateRange(accountNumber, dateFrom, dateTo);
        assertEquals(200L, balance); // 100 + 100
    }

    @Test
    void findStatementsByDateRange_successful() {
        LocalDateTime dateFrom = LocalDateTime.parse("2014-01-01T00:00:00");
        LocalDateTime dateTo = LocalDateTime.parse("2014-01-10T00:00:00");

        List<StatementDAO> statements = Arrays.asList(
                createStatementDAOWithDate(12345L, "2014-01-05T00:00:00"),
                createStatementDAOWithDate(12345L, "2014-01-07T00:00:00")
        );

        when(repository.findByDateRange(dateFrom, dateTo)).thenReturn(statements);

        List<Statement> result = service.findStatementsByDateRange(dateFrom, dateTo);

        assertEquals(2, result.size());
    }

    @Test
    void findStatementsByDateRange_invalid() {
        LocalDateTime dateFrom = LocalDateTime.parse("2014-01-01T00:00:00");
        LocalDateTime dateTo = LocalDateTime.parse("2014-01-10T00:00:00");

        when(repository.findByDateRange(dateFrom, dateTo)).thenReturn(Collections.emptyList());

        List<Statement> result = service.findStatementsByDateRange(dateFrom, dateTo);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAccountBalance_successful() {
        Long accountNumber = 12345L;

        List<StatementDAO> statements = Arrays.asList(
                createStatementDAOWithDate(accountNumber, "2014-01-05T00:00:00"),
                createStatementDAOWithDate(accountNumber, "2014-01-07T00:00:00")
        );

        when(repository.findByAccountNumber(accountNumber)).thenReturn(statements);
        Long balance = service.getAccountBalance(accountNumber);
        assertEquals(200L, balance); // 100 + 100
    }

    private Statement createStatement(Long id){
        Statement statement = new Statement();
        statement.setId(id);
        statement.setAccountNumber(12345L);
        statement.setOperationDate(LocalDateTime.parse("2014-01-05T00:00:00"));
        statement.setBeneficiaryNumber(54321L);
        statement.setComment("test Comment");
        statement.setAmount(100L);
        statement.setCurrency("Test");
        return statement;
    }

    private StatementDAO createStatementDAO(Long id){
        StatementDAO statementDAO = new StatementDAO();
        statementDAO.setId(id);
        statementDAO.setAccountNumber(12345L);
        statementDAO.setOperationDate(LocalDateTime.parse("2014-01-05T00:00:00"));
        statementDAO.setBeneficiaryNumber(54321L);
        statementDAO.setComment("test Comment");
        statementDAO.setAmount(100L);
        statementDAO.setCurrency("Test");
        return statementDAO;
    }

    private StatementDAO createStatementDAOWithDate(Long accountNumber, String date) {
        StatementDAO statementDAO = new StatementDAO();
        statementDAO.setAccountNumber(accountNumber);
        statementDAO.setOperationDate(LocalDateTime.parse(date));
        statementDAO.setAmount(100L);
        return statementDAO;
    }

    private List<Statement> createStatementList(){
        List<Statement> statementList = new ArrayList<>();
        statementList.add(createStatement(1L));
        statementList.add(createStatement(2L));
        return statementList;
    }

    private List<StatementDAO> createStatementDAOList(){
        List<StatementDAO> statementDAOList = new ArrayList<>();
        statementDAOList.add(createStatementDAO(1L));
        statementDAOList.add(createStatementDAO(2L));
        return statementDAOList;
    }

    private MultipartFile createMockMultipartFile(String name, String content) throws IOException {
        return new MockMultipartFile(
                name,
                name,
                "text/csv",
                content.getBytes()
        );
    }
}
