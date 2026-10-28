package com.HenkPretorius.finance_tracker.transaction;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
                request.transactionDate(),
                null
        );
        return repository.save(transaction);
    }

    @GetMapping
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Transaction findById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
