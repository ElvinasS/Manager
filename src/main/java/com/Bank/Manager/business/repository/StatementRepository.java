package com.Bank.Manager.business.repository;

import com.Bank.Manager.business.repository.model.StatementDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepository extends JpaRepository<StatementDAO, Long> {
}
