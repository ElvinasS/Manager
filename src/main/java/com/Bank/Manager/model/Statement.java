package com.Bank.Manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@EqualsAndHashCode
@NoArgsConstructor
@Data
public class Statement {

    private static final String DATE_FORMAT_MESSAGE = "Date format: yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}$";

    private Long id;

    @NotNull
    private Long accountNumber;

    @NotNull
    @Pattern(regexp = DATE_PATTERN, message = DATE_FORMAT_MESSAGE)
    private LocalDateTime operationDate;

    @NotNull
    private Long beneficiaryNumber;

    private String comment;

    @NotNull
    private Long amount;

    @NotNull
    private String currency;
}
