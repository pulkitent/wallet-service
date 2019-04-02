package com.tw.expathashala.walletservice.moneytransaction;

public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public MoneyTransaction save(MoneyTransaction transaction) {
        return transactionRepository.save(transaction);
    }
}
