package com.tw.expathashala.walletservice.moneytransaction;

import com.tw.expathashala.walletservice.wallet.Wallet;

import javax.persistence.*;

@Entity
public class MoneyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public int amount;
    @Enumerated(EnumType.STRING)
    public TransactionType type;

    public MoneyTransaction() {
    }

    public MoneyTransaction(int amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
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
