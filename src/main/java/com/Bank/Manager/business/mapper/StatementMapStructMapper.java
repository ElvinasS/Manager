package com.Bank.Manager.business.mapper;
import com.Bank.Manager.business.repository.model.StatementDAO;
import com.Bank.Manager.model.Statement;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

//@Component
@Mapper(componentModel = "spring")
public interface StatementMapStructMapper {

    StatementDAO statementToStatementDAO(Statement statement);

    Statement statementDAOToStatement(StatementDAO statementDAO);
}
