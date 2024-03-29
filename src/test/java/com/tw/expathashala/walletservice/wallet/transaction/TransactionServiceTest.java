package com.tw.expathashala.walletservice.wallet.transaction;

import com.tw.expathashala.walletservice.transaction.Transaction;
import com.tw.expathashala.walletservice.transaction.TransactionRepository;
import com.tw.expathashala.walletservice.transaction.TransactionService;
import com.tw.expathashala.walletservice.transaction.TransactionType;
import com.tw.expathashala.walletservice.wallet.InvalidTransactionAmountException;
import com.tw.expathashala.walletservice.wallet.Wallet;
import com.tw.expathashala.walletservice.wallet.WalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    private final int limit = Integer.MAX_VALUE;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void fetchTransactionsForWalletWithNameJohn() throws InvalidTransactionAmountException {
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet wallet = walletWithNameJohnAnd1000Balance();
        Transaction transaction = new Transaction(20, TransactionType.DEBIT, "Snacks");
        wallet.process(transaction);
        Wallet savedWallet = walletRepository.save(wallet);

        Transaction transactionOfWallet = transactionService.fetchAll(savedWallet.getId(), limit).get(0);

        assertEquals(transaction.getAmount(), transactionOfWallet.getAmount());
    }

    @Test
    void fetchTransactionsForWalletWithTwoTransaction() throws InvalidTransactionAmountException {
        int transactionAmount = 100;
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet savedWallet = saveWalletWithTwoTransactions();

        Transaction transactionOfWallet = transactionService.fetchAll(savedWallet.getId(), limit).get(0);

        assertEquals(transactionAmount, transactionOfWallet.getAmount());
    }

    @Test
    void fetchTransactionsForWalletWithEmptyTransaction() {
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet wallet = walletWithNameJohnAnd1000Balance();
        Wallet savedWallet = walletRepository.save(wallet);

        assertTrue(transactionService.fetchAll(savedWallet.getId(), limit).isEmpty());
    }

    @Test
    void fetchTransactionsForInvalidWalletId() {
        long invalidWalletId = 999L;
        TransactionService transactionService = new TransactionService(transactionRepository);

        assertTrue(transactionService.fetchAll(invalidWalletId, limit).isEmpty());
    }

    @Test
    void fetchTransactionsHavingRemarksWhenGivenValidWallet() throws InvalidTransactionAmountException {
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet savedWallet = saveWalletWithSingleTransaction();

        Transaction transactionOfWallet = transactionService.fetchAll(savedWallet.getId(), limit).get(0);

        assertEquals("Snacks", transactionOfWallet.getRemark());
    }

    @Test
    void fetchTransactionsHavingDateWhenGivenValidWallet() throws InvalidTransactionAmountException {
        TransactionService transactionService = new TransactionService(transactionRepository);
        final Date oneHourBefore = Date.from(Instant.now().minus(Duration.ofHours(1)));
        Wallet savedWallet = saveWalletWithSingleTransaction();

        Transaction transactionOfWallet = transactionService.fetchAll(savedWallet.getId(), limit).get(0);

        assertTrue(transactionOfWallet.getCreatedAt().after(oneHourBefore));
    }

    @Test
    void expects1TransactionWhenLimitIs1() throws InvalidTransactionAmountException {
        int limit = 1;
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet savedWallet = saveWalletWithTwoTransactions();

        List<Transaction> transactions = transactionService.fetchAll(savedWallet.getId(), limit);

        assertEquals(limit, transactions.size());
    }

    @Test
    void expectsLatestTransactionFromDBWhenLimitIs1() throws InvalidTransactionAmountException {
        int limit = 1;
        int latestTransactionAmount = 100;
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet savedWallet = saveWalletWithTwoTransactions();

        List<Transaction> transactions = transactionService.fetchAll(savedWallet.getId(), limit);

        assertEquals(latestTransactionAmount, transactions.get(0).getAmount());
    }

    private Wallet saveWalletWithSingleTransaction() throws InvalidTransactionAmountException {
        Wallet wallet = walletWithNameJohnAnd1000Balance();
        Transaction firstTransaction = new Transaction(20, TransactionType.DEBIT, "Snacks");
        wallet.process(firstTransaction);
        return walletRepository.save(wallet);
    }

    private Wallet saveWalletWithTwoTransactions() throws InvalidTransactionAmountException {
        Wallet wallet = walletWithNameJohnAnd1000Balance();
        Transaction firstTransaction = new Transaction(20, TransactionType.DEBIT, "Snacks");
        Transaction secondTransaction = new Transaction(100, TransactionType.CREDIT, "Snacks");
        wallet.process(firstTransaction);
        Wallet savedWallet = walletRepository.save(wallet);
        savedWallet.process(secondTransaction);
        return walletRepository.save(savedWallet);
    }

    private Wallet walletWithNameJohnAnd1000Balance() {
        return new Wallet("John", 1000);
    }
}
