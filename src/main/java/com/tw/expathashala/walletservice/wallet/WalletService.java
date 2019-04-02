package com.tw.expathashala.walletservice.wallet;

import com.tw.expathashala.walletservice.moneytransaction.MoneyTransaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public
class WalletService {
    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public Optional<Wallet> findById(long walletId) {
        return walletRepository.findById(walletId);
    }

    public MoneyTransaction addTransaction(Long walletId, MoneyTransaction transaction) {
        Wallet wallet = findById(walletId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        wallet.process(transaction);
        save(wallet);
        return transaction;
    }
}