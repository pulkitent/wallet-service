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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    WalletRepository walletRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void fetchTransactionByWalletId() {
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet wallet = new Wallet("John", 1000);
        Transaction transaction = new Transaction(20, TransactionType.DEBIT);
        wallet.process(transaction);
        Wallet savedWallet = walletRepository.save(wallet);

        Transaction transactionOfWallet = transactionService.fetch(savedWallet.getId()).get().get(0);

        assertEquals(transaction.getAmount(), transactionOfWallet.getAmount());
    }

    @Test
    void fetchAllTransactionsByWalletId() {
        int TRANSACTION_AMOUNT = 20;
        TransactionService transactionService = new TransactionService(transactionRepository);
        Wallet wallet = prepareWalletWithTwoTransactions();
        Wallet savedWallet = walletRepository.save(wallet);

        Transaction transactionOfWallet = transactionService.fetch(savedWallet.getId()).get().get(0);

        assertEquals(TRANSACTION_AMOUNT, transactionOfWallet.getAmount());
    }

    private Wallet prepareWalletWithTwoTransactions() {
        Wallet wallet = new Wallet("John", 1000);
        Transaction firstTransaction = new Transaction(20, TransactionType.DEBIT);
        Transaction secondTransaction = new Transaction(100, TransactionType.CREDIT);
        wallet.process(firstTransaction);
        wallet.process(secondTransaction);
        return wallet;
    }
}
