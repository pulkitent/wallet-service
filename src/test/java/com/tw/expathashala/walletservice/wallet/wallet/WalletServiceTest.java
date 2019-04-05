package com.tw.expathashala.walletservice.wallet.wallet;

import com.tw.expathashala.walletservice.transaction.Transaction;
import com.tw.expathashala.walletservice.transaction.TransactionRepository;
import com.tw.expathashala.walletservice.transaction.TransactionType;
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
        Wallet wallet = walletWithNameJohnAndBalance200();
        Wallet expectedWallet = walletService.save(wallet);

        assertEquals(expectedWallet.getName(), wallet.getName());
        assertEquals(expectedWallet.getBalance(), wallet.getBalance());
    }

    @Test
    void fetchDetailsWhenIdIsGiven() {
        WalletService walletService = new WalletService(walletRepository);
        Wallet wallet = walletWithNameJohnAndBalance200();

        Wallet expectedWallet = walletService.save(wallet);
        Optional<Wallet> findWalletBasedOnId = walletService.findById(wallet.getId());

        assertEquals(expectedWallet.getBalance(), findWalletBasedOnId.get().getBalance());
    }

    @Nested
    class BusinessTransaction {

        @Test
        void addTransactionsLinkedToAWalletOnSaveOfWallet() {
            WalletService walletService = new WalletService(walletRepository);
            Wallet wallet = walletWithNameJohnAndBalance200();
            Transaction firstTransaction = new Transaction(10, TransactionType.CREDIT);
            Transaction secondTransaction = new Transaction(30, TransactionType.DEBIT);
            wallet.process(firstTransaction);
            wallet.process(secondTransaction);

            walletService.save(wallet);

            Assertions.assertEquals(2, transactionRepository.count());
        }
        @Test
        void addTransactionsLinkedToAWalletOnSaveOfWalletAndUpdateBalance() {
            WalletService walletService = new WalletService(walletRepository);
            Wallet wallet = walletWithNameJohnAndBalance200();
            Transaction firstTransaction = new Transaction(10, TransactionType.CREDIT);
            Transaction secondTransaction = new Transaction(30, TransactionType.CREDIT);
            wallet.process(firstTransaction);
            wallet.process(secondTransaction);

            walletService.save(wallet);

            Assertions.assertEquals(2, transactionRepository.count());
        }

        @Test
        void addTransactionsToAWalletAndUpdateBalance() {
            WalletService walletService = new WalletService(walletRepository);
            Wallet wallet = walletWithNameJohnAndBalance200();
            Wallet savedWallet = walletService.save(wallet);
            Transaction firstTransaction = new Transaction(10, TransactionType.CREDIT);

            walletService.addTransaction(savedWallet.getId(),firstTransaction);

            Wallet updatedWallet = walletService.findById(savedWallet.getId()).get();
            Assertions.assertEquals(210, updatedWallet.getBalance());
        }

    }
    private Wallet walletWithNameJohnAndBalance200() {
        return new Wallet("John", 200);
    }
}
