package com.tw.expathashala.walletservice.wallet.transaction;

import com.tw.expathashala.walletservice.transaction.Transaction;
import com.tw.expathashala.walletservice.transaction.TransactionRepository;
import com.tw.expathashala.walletservice.transaction.TransactionService;
import com.tw.expathashala.walletservice.transaction.TransactionType;
import com.tw.expathashala.walletservice.wallet.Wallet;
import com.tw.expathashala.walletservice.wallet.WalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void fetchTransactionsForWalletWithNameJohn() {
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet wallet = walletWithNameJohnAnd1000Balance();
        Transaction transaction = new Transaction(20, TransactionType.DEBIT);
        wallet.process(transaction);
        Wallet savedWallet = walletRepository.save(wallet);

        Transaction transactionOfWallet = transactionService.fetch(savedWallet.getId()).get(0);

        assertEquals(transaction.getAmount(), transactionOfWallet.getAmount());
    }

    @Test
    void fetchTransactionsForWalletWithTwoTransaction() {
        int transactionAmount = 20;
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet wallet = prepareWalletWithTwoTransactions();
        Wallet savedWallet = walletRepository.save(wallet);

        Transaction transactionOfWallet = transactionService.fetch(savedWallet.getId()).get(0);

        assertEquals(transactionAmount, transactionOfWallet.getAmount());
    }

    @Test
    void fetchTransactionsForWalletWithEmptyTransaction() {
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet wallet = walletWithNameJohnAnd1000Balance();
        Wallet savedWallet = walletRepository.save(wallet);

        assertTrue(transactionService.fetch(savedWallet.getId()).isEmpty());
    }

    @Test
    void fetchTransactionsForInvalidWalletId() {
        long invalidWalletId = 999L;
        TransactionService transactionService = new TransactionService(transactionRepository);

        assertTrue(transactionService.fetch(invalidWalletId).isEmpty());
    }

    @Test
    void fetchTransactionsHavingRemarksWhenGivenValidWallet() {
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet wallet = walletWithNameJohnAnd1000Balance();
        Transaction firstTransaction = new Transaction(20, TransactionType.DEBIT, "Snacks");
        wallet.process(firstTransaction);
        Wallet savedWallet = walletRepository.save(wallet);

        Transaction transactionOfWallet = transactionService.fetch(savedWallet.getId()).get(0);

        assertEquals("Snacks", transactionOfWallet.getRemark());
    }

    @Test
    void fetchTransactionsHavingValidDateWhenGivenValidWallet() {
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet wallet = walletWithNameJohnAnd1000Balance();
        Transaction firstTransaction = new Transaction(200, TransactionType.DEBIT, "Movie");
        wallet.process(firstTransaction);
        Wallet savedWallet = walletRepository.save(wallet);

        Transaction transactionOfWallet = transactionService.fetch(savedWallet.getId()).get(0);

        final Date oneHourBefore = Date.from(Instant.now().minus(Duration.ofHours(1)));
        assertTrue(transactionOfWallet.getCreatedAt().after(oneHourBefore));
    }

    private Wallet prepareWalletWithTwoTransactions() {
        Wallet wallet = walletWithNameJohnAnd1000Balance();
        Transaction firstTransaction = new Transaction(20, TransactionType.DEBIT);
        Transaction secondTransaction = new Transaction(100, TransactionType.CREDIT);
        wallet.process(firstTransaction);
        wallet.process(secondTransaction);
        return wallet;
    }

    private Wallet walletWithNameJohnAnd1000Balance() {
        return new Wallet("John", 1000);
    }
}
