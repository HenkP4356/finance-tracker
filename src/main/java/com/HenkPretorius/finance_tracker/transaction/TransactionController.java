package com.HenkPretorius.finance_tracker.transaction;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionRepository repository;

    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@Valid @RequestBody TransactionRequest request) {
        Transaction transaction = new Transaction(
                request.type(),
                request.amount(),
                request.category(),
                request.description(),
                request.transactionDate()
        );
        return repository.save(transaction);
    }

    @GetMapping
    public List<Transaction> findAll() {
        return repository.findAll();
    }
}
