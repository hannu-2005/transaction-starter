package com.example.transactionstarter.transaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.transactionstarter.exception.InvalidStatusTransitionException;
import com.example.transactionstarter.exception.TransactionNotFoundException;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void createTransaction_shouldSaveTransaction() {

        Transaction transaction = new Transaction(
                "TXN001",
                "CUST001",
                new BigDecimal("1000.00"),
                "INR",
                "PAYMENT",
                "PENDING"
        );

        when(transactionRepository.existsById("TXN001"))
                .thenReturn(false);

        when(transactionRepository.save(transaction))
                .thenReturn(transaction);

        Transaction result =
                transactionService.createTransaction(transaction);

        assertEquals("TXN001", result.getTransactionId());
        assertEquals("PENDING", result.getTransactionStatus());

        verify(transactionRepository).save(transaction);
    }

    @Test
    void getTransaction_shouldReturnTransaction() {

        Transaction transaction = new Transaction(
                "TXN002",
                "CUST001",
                new BigDecimal("500.00"),
                "INR",
                "PAYMENT",
                "PENDING"
        );

        when(transactionRepository.findById("TXN002"))
                .thenReturn(Optional.of(transaction));

        Transaction result =
                transactionService.getTransaction("TXN002");

        assertEquals("TXN002", result.getTransactionId());
    }

    @Test
    void getTransaction_shouldThrowExceptionWhenNotFound() {

        when(transactionRepository.findById("TXN999"))
                .thenReturn(Optional.empty());

        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.getTransaction("TXN999")
        );
    }

    @Test
    void updateStatus_shouldAllowValidTransition() {

        Transaction transaction = new Transaction(
                "TXN003",
                "CUST001",
                new BigDecimal("750.00"),
                "INR",
                "PAYMENT",
                "PENDING"
        );

        when(transactionRepository.findById("TXN003"))
                .thenReturn(Optional.of(transaction));

        when(transactionRepository.save(transaction))
                .thenReturn(transaction);

        Transaction result =
                transactionService.updateStatus(
                        "TXN003",
                        "PROCESSING"
                );

        assertEquals("PROCESSING",
                result.getTransactionStatus());

        verify(transactionRepository).save(transaction);
    }

    @Test
    void updateStatus_shouldRejectInvalidTransition() {

        Transaction transaction = new Transaction(
                "TXN004",
                "CUST001",
                new BigDecimal("900.00"),
                "INR",
                "PAYMENT",
                "COMPLETED"
        );

        when(transactionRepository.findById("TXN004"))
                .thenReturn(Optional.of(transaction));

        assertThrows(
                InvalidStatusTransitionException.class,
                () -> transactionService.updateStatus(
                        "TXN004",
                        "PENDING"
                )
        );

        verify(transactionRepository, never())
                .save(transaction);
    }

    @Test
    void getCustomerTransactions_shouldReturnCustomerTransactions() {

        Transaction transaction1 = new Transaction(
                "TXN005",
                "CUST100",
                new BigDecimal("100.00"),
                "INR",
                "PAYMENT",
                "PENDING"
        );

        Transaction transaction2 = new Transaction(
                "TXN006",
                "CUST100",
                new BigDecimal("200.00"),
                "INR",
                "TRANSFER",
                "PROCESSING"
        );

        when(transactionRepository.findByCustomerId("CUST100"))
                .thenReturn(List.of(transaction1, transaction2));

        List<Transaction> result =
                transactionService.getCustomerTransactions("CUST100");

        assertEquals(2, result.size());
        assertEquals("CUST100",
                result.get(0).getCustomerId());
    }
}