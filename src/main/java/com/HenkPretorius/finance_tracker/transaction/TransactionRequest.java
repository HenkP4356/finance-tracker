package com.HenkPretorius.finance_tracker.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(@NotNull TransactionType type,
                                 @Positive @NotNull BigDecimal amount,
                                 @NotBlank @Size(max = 50) String category,
                                 @Size(max = 255) String description,
                                 @NotNull LocalDate transactionDate) {

}
