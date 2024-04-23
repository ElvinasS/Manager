package com.Bank.Manager.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@EqualsAndHashCode
@NoArgsConstructor
@Data
public class Statement {

    private static final String DATE_FORMAT_MESSAGE = "Date format: yyyy-mm-dd-hh-mm";
    private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}$";

    private Long id;

    @NotNull
    private Long accountNumber;

    @NotNull
    @Pattern(regexp = DATE_PATTERN, message = DATE_FORMAT_MESSAGE)
    private String operationDate;

    @NotNull
    private Long beneficiaryNumber;

    private String comment;
    
    @NotNull
    private Long amount;

    @NotNull
    private String currency;

}
