package com.Bank.Manager.business.repository;

import com.Bank.Manager.business.repository.model.StatementDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<StatementDAO, Long> {

    @Query("SELECT statement FROM StatementDAO statement WHERE statement.operationDate BETWEEN :dateFrom AND :dateTo")
    List<StatementDAO> findByDateRange(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

}
