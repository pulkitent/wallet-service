package com.tw.expathashala.walletservice.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tw.expathashala.walletservice.wallet.Wallet;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

//Represents exchange of money
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Positive(message = "Amount should be greater than zero")
    @Max(value = 10000)
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
