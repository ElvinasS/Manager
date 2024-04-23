package com.Bank.Manager.business.mapper;
import com.Bank.Manager.business.repository.model.StatementDAO;
import com.Bank.Manager.model.Statement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatementMapStructMapper {

    StatementDAO statementToStatementDAO(Statement statement);

    Statement statementDAOToStatement(StatementDAO statementDAO);
}
