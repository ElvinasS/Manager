package com.Bank.Manager.controller;


import com.Bank.Manager.business.service.StatementService;
import com.Bank.Manager.business.utils.CsvUtils;
import com.Bank.Manager.model.Statement;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Statement> saveStatement(@Valid @RequestBody Statement statement, BindingResult bindingResult) throws Exception {
        log.info("Create new statement by passing : {}", statement);
        if (bindingResult.hasErrors()) {
            log.error("New statement is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        Statement statementSaved = statementService.saveStatement(statement);
        log.debug("New statement is created: {}", statement);
        return new ResponseEntity<>(statementSaved, HttpStatus.CREATED);
    }

    @PostMapping("/uploadCsv")
    public ResponseEntity<String> uploadCsv(@RequestParam("files") List<MultipartFile> files) {
        try {
            statementService.saveStatementsFromMultipleFiles(files);
            return new ResponseEntity<>("Files uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();

            // Return error response
            return new ResponseEntity<>("Failed to upload files: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
