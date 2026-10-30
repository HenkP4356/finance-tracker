package com.HenkPretorius.finance_tracker.transaction;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionRepository repository;
    private final TransactionImportService importService;

    public TransactionController(TransactionRepository repository, TransactionImportService importService) {
        this.repository = repository;
        this.importService = importService;
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

    @PostMapping("/import")
    public ImportResult importFnb(@RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        List<String> lines = content.lines().toList();
        return new ImportResult(importService.importFNB(lines));
    }

    @PutMapping("/{id}")
    public Transaction update(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        transaction.update(
                request.type(),
                request.amount(),
                request.category(),
                request.description(),
                request.transactionDate()
        );

        return repository.save(transaction);
    }

}
