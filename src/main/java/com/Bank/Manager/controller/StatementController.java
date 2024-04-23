package com.Bank.Manager.controller;


import com.Bank.Manager.business.service.StatementService;
import com.Bank.Manager.model.Statement;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/Statement")
public class StatementController {

    @Autowired
    StatementService statementService;

    @GetMapping
    public ResponseEntity<List<Statement>> findAllStatement() {
        log.info("Retrieve list of statements");
        List<Statement> statementList = statementService.findAllStatements();
        if (statementList.isEmpty()) {
            log.warn("Statement list is empty! {}", statementList);
            return ResponseEntity.notFound().build();
        }
        log.debug("Statement list is found. Size: {}", statementList::size);
        return ResponseEntity.ok(statementList);
    }
}
