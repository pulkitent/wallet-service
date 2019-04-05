package com.tw.expathashala.walletservice.transaction;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Optional<List<Transaction>> fetch(Long id) {
        return transactionRepository.findByWalletId(id);
    }
}
