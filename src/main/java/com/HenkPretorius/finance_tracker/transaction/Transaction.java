package com.HenkPretorius.finance_tracker.transaction;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDate transactionDate;
    private String importReference;

    protected Transaction(){
    }

    public Transaction(TransactionType type, BigDecimal amount, String category, String description, LocalDate transactionDate, String importReference) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.transactionDate = transactionDate;
        this.importReference = importReference;
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public String getImportReference() { return importReference; }
}


