package com.Bank.Manager.controller;

import com.Bank.Manager.business.service.StatementService;
import com.Bank.Manager.business.utils.CsvUtils;
import com.Bank.Manager.model.Statement;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    @GetMapping("/exportCsv")
    public ResponseEntity<byte[]> exportStatementsAsCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        try {
            List<Statement> statements;

            if (dateFrom != null && dateTo != null) {
                statements = statementService.findStatementsByDateRange(dateFrom, dateTo);
            } else {
                statements = statementService.findAllStatements();
            }

            byte[] csvData = CsvUtils.convertStatementsToCsv(statements);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("statements.csv").build());

            return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();

            // Return error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<Long> getAccountBalanceByDateRange(
            @RequestParam Long accountNumber,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTo) {


        if (dateFrom == null || dateTo == null) {
            Long balance = statementService.getAccountBalance(accountNumber);
            return ResponseEntity.ok(balance);
        }
        else {
            Long balance = statementService.getAccountBalanceByDateRange(accountNumber, dateFrom, dateTo);
            return ResponseEntity.ok(balance);
        }
    }
}
