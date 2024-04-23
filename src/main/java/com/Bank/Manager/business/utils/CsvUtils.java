package com.Bank.Manager.business.utils;

import com.Bank.Manager.model.Statement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static List<Statement> parseCsvFile(InputStream inputStream) throws Exception {
        List<Statement> statements = new ArrayList<>();

        try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord csvRecord : csvParser) {
                Statement statement = new Statement();
                statement.setAccountNumber(Long.parseLong(csvRecord.get("account_number")));
                statement.setOperationDate(LocalDateTime.parse(csvRecord.get("operation_date"), DATE_FORMATTER));
                statement.setBeneficiaryNumber(Long.parseLong(csvRecord.get("beneficiary_number")));
                statement.setComment(csvRecord.get("comment"));
                statement.setAmount(Long.parseLong(csvRecord.get("amount")));
                statement.setCurrency(csvRecord.get("currency"));

                statements.add(statement);
            }
        }

        return statements;
    }
}
