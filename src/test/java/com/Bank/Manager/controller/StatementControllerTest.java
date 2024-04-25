package com.Bank.Manager.controller;

import com.Bank.Manager.business.service.StatementService;
import com.Bank.Manager.model.Statement;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class StatementControllerTest {

    public static String URL = "/api/v1/Statement";


    @MockBean
    private StatementService service;

    @Autowired
    private StatementController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllStatement_successful() throws Exception {

        List<Statement> statementList = createStatementList();

        when(service.findAllStatements()).thenReturn(statementList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountNumber").value(12345L))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllStatements();
    }

    @Test
    void uploadCsv_successful() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "file1.csv",
                "text/csv",
                "account_number,operation_date,beneficiary_number,comment,amount,currency\n1234,2024-01-01T10:00:00,5678,test,100,test".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/Statement/uploadCsv")
                        .file(file1))
                .andExpect(status().isOk())
                .andExpect(content().string("Files uploaded successfully"));
    }

    @Test
    void exportStatementsAsCsv_successful() throws Exception {
        List<Statement> statementList = createStatementList();

        when(service.findStatementsByDateRange(any(), any())).thenReturn(statementList);

        mockMvc.perform(get("/api/v1/Statement/exportCsv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("text/csv")))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"statements.csv\""));
    }

    @Test
    void getAccountBalanceByDateRange_successful() throws Exception {
        Long accountNumber = 12345L;
        LocalDateTime dateFrom = LocalDateTime.parse("2014-01-01T00:00:00");
        LocalDateTime dateTo = LocalDateTime.parse("2014-01-10T00:00:00");
        Long balance = 300L;

        when(service.getAccountBalanceByDateRange(accountNumber, dateFrom, dateTo)).thenReturn(balance);

        mockMvc.perform(get("/api/v1/Statement/balance")
                        .param("accountNumber", accountNumber.toString())
                        .param("dateFrom", dateFrom.toString())
                        .param("dateTo", dateTo.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(balance.toString()));
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

    private List<Statement> createStatementList(){
        List<Statement> statementList = new ArrayList<>();
        statementList.add(createStatement(1L));
        statementList.add(createStatement(2L));
        return statementList;
    }
}
