package com.tw.expathashala.walletservice.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tw.expathashala.walletservice.wallet.Wallet;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

//Represents exchange of money
@Entity
public class Transaction {

    public static final String MESSAGE_NEGATIVE_AMOUNT = "Amount should be greater than zero";
    public static final String MAX_AMOUNT_ALLOWED_EXCEEDED_MESSAGE = "Amount should not exceed ₹ 10000";
    private static final int MAX_AMOUNT_ALLOWED = 10000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Positive(message = MESSAGE_NEGATIVE_AMOUNT)
    @Max(value = MAX_AMOUNT_ALLOWED, message = MAX_AMOUNT_ALLOWED_EXCEEDED_MESSAGE)
    private int amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private Wallet wallet;

    public Transaction() {
    }

    public Transaction(int amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public int amountToUpdate() {
        return type.amountToUpdate(amount);
    }
}
