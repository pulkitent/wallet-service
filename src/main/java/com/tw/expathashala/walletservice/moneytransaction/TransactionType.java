package com.tw.expathashala.walletservice.moneytransaction;

public enum TransactionType {
    CREDIT(1), DEBIT(-1);

    private final int operation;

    TransactionType(int operation) {
        this.operation = operation;
    }

    public int amountToUpdate(int amount) {
        return amount * operation;
    }
}
