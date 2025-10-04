package services;

import model.Transaction;
import utils.Enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionLogger {

    private final List<Transaction> transactions;

    public TransactionLogger() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Log a transaction
     *
     * @param transaction Transaction object to log
     */
    public void logTransaction(Transaction transaction) {
        transactions.add(transaction);
        System.out.println("Transaction logged: " + transaction.toString());
    }

    /**
     * Return all logged transactions
     *
     * @return unmodifiable list of transactions
     */
    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /**
     * Find a transaction by ID
     *
     * @param transactionId UUID of the transaction
     * @return Transaction object or null if not found
     */
    public Transaction findTransactionById(String transactionId) {
        return transactions.stream()
                .filter(txn -> txn.getTransactionId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get transactions by status
     *
     * @param status TransactionStatus
     * @return list of transactions with matching status
     */
    public List<Transaction> getTransactionsByStatus(TransactionStatus status) {
        return transactions.stream()
                .filter(txn -> txn.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Get latest N transactions
     *
     * @param n number of latest transactions to return
     * @return list of latest N transactions
     */
    public List<Transaction> getLatestTransactions(int n) {
        int size = transactions.size();
        if (n <= 0 || size == 0) return Collections.emptyList();
        return transactions.subList(Math.max(0, size - n), size);
    }

    /**
     * Get transactions after a certain timestamp
     *
     * @param timestamp LocalDateTime threshold
     * @return list of transactions after the given timestamp
     */
    public List<Transaction> getTransactionsAfter(LocalDateTime timestamp) {
        return transactions.stream()
                .filter(txn -> txn.getTimestamp().isAfter(timestamp))
                .collect(Collectors.toList());
    }

    /**
     * Clear all transactions (useful for testing)
     */
    public void clearTransactions() {
        transactions.clear();
    }
}