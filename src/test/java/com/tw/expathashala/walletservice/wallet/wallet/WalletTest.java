package com.tw.expathashala.walletservice.wallet.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.expathashala.walletservice.transaction.Transaction;
import com.tw.expathashala.walletservice.transaction.TransactionType;
import com.tw.expathashala.walletservice.wallet.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class WalletTest {

    @Test
    void serializationOfWallet() throws JsonProcessingException {
        Wallet wallet = new Wallet("Alice", 100);
        String expectedString = "{\"id\":null,\"name\":\"Alice\",\"balance\":100}";
        ObjectMapper objectMapper = new ObjectMapper();

        String walletString = objectMapper.writeValueAsString(wallet);

        assertEquals(expectedString, walletString);
    }

    @Test
    void deserializationOfWallet() throws IOException {
        String walletString = "{\"name\":\"Alice\",\"balance\":100}";
        ObjectMapper objectMapper = new ObjectMapper();
        Wallet expectedWallet = new Wallet("Alice", 100);

        Wallet wallet = objectMapper.readValue(walletString, Wallet.class);

        assertEquals(expectedWallet.getBalance(), wallet.getBalance());
        assertEquals(expectedWallet.getName(), wallet.getName());
    }

    @Test
    void addTransactionAndUpdateBalance() throws IOException {
        Wallet wallet = new Wallet("Akila",1000);
        Transaction firstTransaction = new Transaction(10, TransactionType.CREDIT);
        Transaction secondTransaction = new Transaction(30, TransactionType.DEBIT);
        wallet.process(firstTransaction);
        wallet.process(secondTransaction);

        assertEquals(980,wallet.getBalance());
    }
}
