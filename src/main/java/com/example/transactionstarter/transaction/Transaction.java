package com.example.transactionstarter.transaction;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")

public class Transaction {
	
	
	    @Id
	    @NotBlank(message = "Transaction ID is required")
	    private String transactionId;

	    @NotBlank(message = "Customer ID is required")
	    private String customerId;

	    @NotNull(message = "Amount is required")
	    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
	    private BigDecimal amount;

	    @NotBlank(message = "Currency is required")
	    @Size(min = 3, max = 3, message = "Currency must be exactly 3 characters")
	    @jakarta.validation.constraints.Pattern(
	            regexp = "^[A-Z]{3}$",
	            message = "Currency must be a 3-letter uppercase code"
	    )
	    private String currency;

	    @NotBlank(message = "Transaction type is required")
	    private String transactionType;

	    @NotBlank(message = "Transaction status is required")
	    private String transactionStatus;

	    public Transaction() {
	    }

	    public Transaction(String transactionId, String customerId,
	                       BigDecimal amount, String currency,
	                       String transactionType, String transactionStatus) {
	        this.transactionId = transactionId;
	        this.customerId = customerId;
	        this.amount = amount;
	        this.currency = currency;
	        this.transactionType = transactionType;
	        this.transactionStatus = transactionStatus;
	    }

	    public String getTransactionId() {
	        return transactionId;
	    }

	    public void setTransactionId(String transactionId) {
	        this.transactionId = transactionId;
	    }

	    public String getCustomerId() {
	        return customerId;
	    }

	    public void setCustomerId(String customerId) {
	        this.customerId = customerId;
	    }

	    public BigDecimal getAmount() {
	        return amount;
	    }

	    public void setAmount(BigDecimal amount) {
	        this.amount = amount;
	    }

	    public String getCurrency() {
	        return currency;
	    }

	    public void setCurrency(String currency) {
	        this.currency = currency;
	    }

	    public String getTransactionType() {
	        return transactionType;
	    }

	    public void setTransactionType(String transactionType) {
	        this.transactionType = transactionType;
	    }

	    public String getTransactionStatus() {
	        return transactionStatus;
	    }

	    public void setTransactionStatus(String transactionStatus) {
	        this.transactionStatus = transactionStatus;
	    }
	}


