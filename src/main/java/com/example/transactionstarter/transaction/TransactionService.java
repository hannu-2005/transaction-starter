package com.example.transactionstarter.transaction;

import java.util.List;

import com.example.transactionstarter.exception.InvalidStatusTransitionException;
import com.example.transactionstarter.exception.TransactionNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction) {

        if (transactionRepository.existsById(transaction.getTransactionId())) {
            throw new IllegalArgumentException("Transaction ID already exists");
        }

        if (!transaction.getTransactionStatus().equals("PENDING")) {
            throw new IllegalArgumentException(
                    "New transaction must have PENDING status");
        }

        String transactionType = transaction.getTransactionType();

        if (!transactionType.equals("PAYMENT")
                && !transactionType.equals("REFUND")
                && !transactionType.equals("TRANSFER")) {

            throw new IllegalArgumentException(
                    "Invalid transaction type: " + transactionType);
        }

        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(String transactionId) {

        return transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new TransactionNotFoundException(
                                "Transaction not found: " + transactionId));
    }

    public Transaction updateStatus(String transactionId, String newStatus) {

        Transaction transaction = getTransaction(transactionId);

        String currentStatus = transaction.getTransactionStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from "
                            + currentStatus + " to " + newStatus);
        }
        
        

        transaction.setTransactionStatus(newStatus);

        return transactionRepository.save(transaction);
    }

    private boolean isValidStatusTransition(
            String currentStatus, String newStatus) {

        if (currentStatus.equals(newStatus)) {
            return true;
        }

        return switch (currentStatus) {

            case "PENDING" ->
                    newStatus.equals("PROCESSING")
                            || newStatus.equals("FAILED");

            case "PROCESSING" ->
                    newStatus.equals("COMPLETED")
                            || newStatus.equals("FAILED");

            case "COMPLETED", "FAILED" ->
                    false;

            default ->
                    false;
        };
    }
    
    public List<Transaction> getCustomerTransactions(String customerId) {

        return transactionRepository.findByCustomerId(customerId);
    }
}