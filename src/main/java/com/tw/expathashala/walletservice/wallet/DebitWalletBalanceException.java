package com.tw.expathashala.walletservice.wallet;

public class DebitWalletBalanceException extends Throwable {
    static final String AMOUNT_CAN_NOT_EXCEED_WALLET_BALANCE = "Amount can not exceed wallet balance";

    DebitWalletBalanceException(String message) {
        super(message);
    }
}
