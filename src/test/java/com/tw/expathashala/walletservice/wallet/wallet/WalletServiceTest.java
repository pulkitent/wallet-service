package com.tw.expathashala.walletservice.wallet.wallet;

import com.tw.expathashala.walletservice.moneytransaction.MoneyTransaction;
import com.tw.expathashala.walletservice.moneytransaction.TransactionRepository;
import com.tw.expathashala.walletservice.moneytransaction.TransactionType;
import com.tw.expathashala.walletservice.wallet.Wallet;
import com.tw.expathashala.walletservice.wallet.WalletRepository;
import com.tw.expathashala.walletservice.wallet.WalletService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WalletServiceTest {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void saveTheDataInDataBase() {
        WalletService walletService = new WalletService(walletRepository);
        Wallet wallet = new Wallet("abc", 200);
        Wallet expectedWallet = walletService.save(wallet);

        assertEquals(expectedWallet.getName(), wallet.getName());
        assertEquals(expectedWallet.getBalance(), wallet.getBalance());
    }

    @Test
    void fetchDetailsWhenIdIsGiven() {
        WalletService walletService = new WalletService(walletRepository);
        Wallet wallet = new Wallet("abc", 200);

        Wallet expectedWallet = walletService.save(wallet);
        Optional<Wallet> findWalletBasedOnId = walletService.findById(wallet.getId());

        assertEquals(expectedWallet.getBalance(), findWalletBasedOnId.get().getBalance());
    }

    @Nested
    class BusinessTransaction {
        @Test
        void addTransactionsLinkedToAWalletOnSaveOfWallet() {
            WalletService walletService = new WalletService(walletRepository);
            Wallet wallet = new Wallet("Ved", 200);
            MoneyTransaction firstTransaction = new MoneyTransaction(10, TransactionType.CREDIT);
            MoneyTransaction secondTransaction = new MoneyTransaction(30, TransactionType.DEBIT);
            wallet.process(firstTransaction);
            wallet.process(secondTransaction);

            walletService.save(wallet);

            Assertions.assertEquals(2, transactionRepository.count());
        }

        @Test
        void addTransactionsLinkedToAWalletOnSaveOfWalletAndUpdateBalance() {
            WalletService walletService = new WalletService(walletRepository);
            Wallet wallet = new Wallet("Ved", 200);
            MoneyTransaction firstTransaction = new MoneyTransaction(10, TransactionType.CREDIT);
            MoneyTransaction secondTransaction = new MoneyTransaction(30, TransactionType.CREDIT);
            wallet.process(firstTransaction);
            wallet.process(secondTransaction);

            walletService.save(wallet);

            Assertions.assertEquals(2, transactionRepository.count());
        }

        @Test
        void addTransactionsToAWalletAndUpdateBalance() {
            WalletService walletService = new WalletService(walletRepository);
            Wallet wallet = new Wallet("Ved", 200);
            Wallet savedWallet = walletService.save(wallet);
            MoneyTransaction firstTransaction = new MoneyTransaction(10, TransactionType.CREDIT);

            walletService.addTransaction(savedWallet.getId(),firstTransaction);

            Wallet updatedWallet = walletService.findById(savedWallet.getId()).get();
            Assertions.assertEquals(210, updatedWallet.getBalance());
        }
    }
}
