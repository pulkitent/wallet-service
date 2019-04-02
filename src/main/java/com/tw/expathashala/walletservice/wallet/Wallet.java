package com.tw.expathashala.walletservice.wallet;

import com.tw.expathashala.walletservice.transaction.Transaction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Represents money holder
@Entity
@ApiModel(description = "An entity to describe wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Database generated id for wallet", required = true)
    private Long id;
    private String name;
    private int balance;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
    public List<Transaction> transaction;

    public Wallet() {
    }

    public Wallet(String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.transaction = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public Long getId() {
        return id;
    }


    public void process(Transaction transaction) {
        this.transaction.add(transaction);
        int amountToUpdate = transaction.amountToUpdate();
        updateBalance(amountToUpdate);
        transaction.setWallet(this);
    }

    private void updateBalance(int amountToUpdate) {
        balance = balance + amountToUpdate;
    }
}
