package com.tw.expathashala.walletservice.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tw.expathashala.walletservice.transaction.Transaction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.tw.expathashala.walletservice.wallet.DebitWalletBalanceException.AMOUNT_CAN_NOT_EXCEED_WALLET_BALANCE;

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


    public void process(Transaction transaction) throws DebitWalletBalanceException {
        this.transaction.add(transaction);
        int amountToUpdate = transaction.amountToUpdate();
        updateBalance(amountToUpdate);
        transaction.setWallet(this);
    }

    private Boolean isDebitPossible(int amountToUpdate) {
        return balance + amountToUpdate >= 0;
    }

    private void updateBalance(int amountToUpdate) throws DebitWalletBalanceException {
        if (!isDebitPossible(amountToUpdate)) {
            throw new DebitWalletBalanceException(AMOUNT_CAN_NOT_EXCEED_WALLET_BALANCE);
        }
        balance += amountToUpdate;
    }
}