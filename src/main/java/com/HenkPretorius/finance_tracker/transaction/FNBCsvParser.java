package com.HenkPretorius.finance_tracker.transaction;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FNBCsvParser {

    public List<Transaction> parse(List<String> lines) {
        boolean headerFound = false;
        List<Transaction> transactions = new ArrayList<>();

        for (String line : lines) {

            if (line.startsWith("Date")) {
                headerFound = true;
                continue;
            }

            if (!headerFound || line.isBlank()) {
                continue;
            }

            String[] parts = line.split(",", 4);

            String dateText = parts[0].trim();
            String amountText = parts[1].trim();
            String balanceText = parts[2].trim();
            String descriptionText = parts[3].trim();

            LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            BigDecimal amount = new BigDecimal(amountText);

            TransactionType type = TransactionType.EXPENSE;

            if (amount.signum() > 0) {
                type = TransactionType.INCOME;
            }

            String importReference = dateText + " | " + amountText + " | " + balanceText + " | " + descriptionText;
            String description = descriptionText.replaceAll("\\s+", " ");

            Transaction transaction = new Transaction(type, amount.abs(), "Uncategorised", description, date, importReference);

            transactions.add(transaction);
        }

        return transactions;
    }

}
