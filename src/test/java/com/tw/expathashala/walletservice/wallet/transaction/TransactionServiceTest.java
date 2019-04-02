package com.tw.expathashala.walletservice.wallet.transaction;

import com.tw.expathashala.walletservice.transaction.Transaction;
import com.tw.expathashala.walletservice.transaction.TransactionRepository;
import com.tw.expathashala.walletservice.transaction.TransactionService;
import com.tw.expathashala.walletservice.transaction.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void saveTheDataInDataBase() {
        TransactionService transactionService = new TransactionService(transactionRepository);
        Transaction transaction = new Transaction(20, TransactionType.DEBIT);
        Transaction moneyTransaction = transactionService.save(transaction);

        assertEquals(transaction.getAmount(),moneyTransaction.getAmount());
    }

}
