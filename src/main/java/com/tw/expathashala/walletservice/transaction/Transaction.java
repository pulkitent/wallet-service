package com.tw.expathashala.walletservice.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tw.expathashala.walletservice.wallet.Wallet;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;

//Represents exchange of money
@Entity
public class Transaction {

    public static final String MESSAGE_NEGATIVE_AMOUNT = "Amount should be greater than zero";
    public static final String MAX_AMOUNT_ALLOWED_EXCEEDED_MESSAGE = "Amount should not exceed ₹ 10000";
    private static final int MAX_AMOUNT_ALLOWED = 10000;
    private static final String REMARKS_CAN_NOT_BE_EMPTY = "remarks can not be empty";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Positive(message = MESSAGE_NEGATIVE_AMOUNT)
    @Max(value = MAX_AMOUNT_ALLOWED, message = MAX_AMOUNT_ALLOWED_EXCEEDED_MESSAGE)
    private int amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Date createdAt;

    @Size(min = 1, max = 50)
    @NotEmpty(message = REMARKS_CAN_NOT_BE_EMPTY)
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private Wallet wallet;

    public Transaction() {
    }

    public Transaction(int amount, TransactionType type, String remark) {
        this.amount = amount;
        this.type = type;
        this.remark = remark;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getRemark() {
        return this.remark;
    }

    @PrePersist
    public void createdAtNow() {
        this.createdAt = new Date();
    }
}
