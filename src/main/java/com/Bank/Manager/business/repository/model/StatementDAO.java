package com.Bank.Manager.business.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "statement")
public class StatementDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number")
    private Long accountNumber;

    @Column(name = "operation_date")
    private String operationDate;

    @Column(name = "beneficiary_number")
    private Long beneficiaryNumber;

    @Column(name = "comment")
    private String comment;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "currency")
    private String currency;

    private StatementDAO(Long statemnt){

    }
}
