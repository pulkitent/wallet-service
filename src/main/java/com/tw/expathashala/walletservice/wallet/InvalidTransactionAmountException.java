package com.tw.expathashala.walletservice.wallet;

public class InvalidTransactionAmountException extends Throwable {
    static final String AMOUNT_CAN_NOT_EXCEED_WALLET_BALANCE = "Amount can not exceed wallet balance";

    InvalidTransactionAmountException(String message) {
        super(message);
    }
}
