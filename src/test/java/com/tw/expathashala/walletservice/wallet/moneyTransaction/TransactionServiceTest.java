package com.tw.expathashala.walletservice.wallet.moneyTransaction;

import com.tw.expathashala.walletservice.moneytransaction.MoneyTransaction;
import com.tw.expathashala.walletservice.moneytransaction.TransactionRepository;
import com.tw.expathashala.walletservice.moneytransaction.TransactionService;
import com.tw.expathashala.walletservice.moneytransaction.TransactionType;
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
        MoneyTransaction transaction = new MoneyTransaction(20, TransactionType.DEBIT);
        MoneyTransaction moneyTransaction = transactionService.save(transaction);

        assertEquals(transaction.getAmount(),moneyTransaction.getAmount());
    }

}
