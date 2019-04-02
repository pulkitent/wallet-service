package com.tw.expathashala.walletservice.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tw.expathashala.walletservice.wallet.Wallet;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Positive(message = "Amount should be greater than zero")
    private int amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public Transaction() {
    }

    public Transaction(int amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private Wallet wallet;

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
