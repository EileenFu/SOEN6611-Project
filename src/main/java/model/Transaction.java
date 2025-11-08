package model;

import utils.Enums.PaymentMethod;
import utils.Enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private final String transactionId;
    private final LocalDateTime timestamp;
    private final double price;
    private final PaymentMethod paymentMethod;
    private TransactionStatus status;
    private final Product product;

    // Constructor
    public Transaction(double price, PaymentMethod paymentMethod, Product product) {
        this.transactionId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.product = product;
        this.status = TransactionStatus.SUCCESS; // default status
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getPrice() { return price; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public Product getProduct() { return product; }
    public TransactionStatus getStatus() { return status; }

    // Setter for status, e.g., to update on failure/refund
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", timestamp=" + timestamp +
                ", price=" + price +
                ", paymentMethod=" + paymentMethod +
                ", status=" + status +
                ", product=" + product +
                '}';
    }
}