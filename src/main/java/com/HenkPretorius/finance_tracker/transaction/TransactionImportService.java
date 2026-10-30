package com.HenkPretorius.finance_tracker.transaction;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionImportService {

    private final FNBCsvParser parser;
    private final TransactionRepository repository;

    public TransactionImportService(FNBCsvParser parser, TransactionRepository repository) {
        this.parser = parser;
        this.repository = repository;
    }

    public int importFNB(List<String> lines) {
        int count = 0;
        List<Transaction> transactions = parser.parse(lines);

        for (Transaction transaction : transactions) {
            if (repository.existsByImportReference(transaction.getImportReference())) {
                continue;
            }

            repository.save(transaction);
            count++;
        }

        return count;
    }
}
