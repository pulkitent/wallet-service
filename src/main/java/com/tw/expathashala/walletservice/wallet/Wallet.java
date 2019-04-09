package com.tw.expathashala.walletservice.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    static final String DEBIT_AMOUNT_LESS_THAN_BALANCE = "Wallet Balance is less than debit amount";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Database generated id for wallet", required = true)
    private Long id;
    private String name;
    private int balance;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
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


    public void process(Transaction transaction) throws DebitException {
        this.transaction.add(transaction);
        int amountToUpdate = transaction.amountToUpdate();
        updateBalance(amountToUpdate);
        transaction.setWallet(this);
    }

    private Boolean isDebitNotPossible(int amountToUpdate) {
        return amountToUpdate < 0 && balance < Math.abs(amountToUpdate);
    }

    private void updateBalance(int amountToUpdate) throws DebitException {
        if (isDebitNotPossible(amountToUpdate)) {
            throw new DebitException(DEBIT_AMOUNT_LESS_THAN_BALANCE);
        }
        balance += amountToUpdate;
    }
}
