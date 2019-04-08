package com.tw.expathashala.walletservice.transaction;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> fetch(Long id, int limit) {
        return transactionRepository.findByWalletIdOrderByCreatedAtDesc(id , PageRequest.of(0, limit));
    }
}
