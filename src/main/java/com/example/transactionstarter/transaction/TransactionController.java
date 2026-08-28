package com.example.transactionstarter.transaction;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.UpdateStatusRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // 1. Create transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        Transaction transaction = new Transaction();

        transaction.setTransactionId(request.getTransactionId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setTransactionStatus(request.getTransactionStatus());

        Transaction createdTransaction =
                transactionService.createTransaction(transaction);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTransaction);
    }

    // 2. Get transaction
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(
            @PathVariable String transactionId) {

        return ResponseEntity.ok(
                transactionService.getTransaction(transactionId));
    }

    // 3. Update transaction status
    @PatchMapping("/{transactionId}/status")
    public ResponseEntity<Transaction> updateStatus(
            @PathVariable String transactionId,
            @Valid @RequestBody UpdateStatusRequest request) {

        Transaction updatedTransaction =
                transactionService.updateStatus(
                        transactionId,
                        request.getTransactionStatus());

        return ResponseEntity.ok(updatedTransaction);
    }

    // 4. Get all transactions for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getCustomerTransactions(
            @PathVariable String customerId) {

        return ResponseEntity.ok(
                transactionService.getCustomerTransactions(customerId));
    }
}