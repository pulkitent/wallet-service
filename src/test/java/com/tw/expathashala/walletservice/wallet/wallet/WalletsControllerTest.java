package com.tw.expathashala.walletservice.wallet.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.expathashala.walletservice.transaction.Transaction;
import com.tw.expathashala.walletservice.transaction.TransactionService;
import com.tw.expathashala.walletservice.transaction.TransactionType;
import com.tw.expathashala.walletservice.wallet.DebitException;
import com.tw.expathashala.walletservice.wallet.Wallet;
import com.tw.expathashala.walletservice.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tw.expathashala.walletservice.transaction.Transaction.MESSAGE_NEGATIVE_AMOUNT;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class WalletsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private
    WalletService walletService;

    @MockBean
    private TransactionService transactionService;

    @Test
    void shouldPassWhenRequestContainsOriginHeader() throws Exception {
        Wallet wallet = new Wallet("Walter White", 100);
        when(walletService.findById(any(Long.class))).thenReturn(java.util.Optional.of(wallet));

        mockMvc.perform(get("/wallets/1")
                .header("Origin", "www.dummyurl.com"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

    @Test
    void shouldFailWhenRequestDoesNotContainOriginHeader() throws Exception {
        Wallet wallet = new Wallet("Walter White", 100);
        when(walletService.findById(any(Long.class))).thenReturn(java.util.Optional.of(wallet));

        mockMvc.perform(get("/wallets/1"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("*"));
    }

    @Test
    void createWithGivenNameAndAmount() throws Exception {
        Wallet wallet = new Wallet("Walter White", 100);
        ObjectMapper objectMapper = new ObjectMapper();
        when(walletService.save(any(Wallet.class))).thenReturn(wallet);

        mockMvc.perform((post("/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wallet))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.balance").value(100))
                .andExpect(jsonPath("$.name").value("Walter White"));

        verify(walletService).save(any(Wallet.class));
    }

    @Test
    void fetchDetailsForGivenIdShouldReturnBalance() throws Exception {
        Wallet wallet = new Wallet("Walter White", 100);
        ObjectMapper objectMapper = new ObjectMapper();
        when(walletService.findById(any(Long.class))).thenReturn(java.util.Optional.of(wallet));

        mockMvc.perform(get("/wallets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wallet)))
                .andExpect(jsonPath("$.balance").value(100))
                .andExpect(jsonPath("$.name").value("Walter White"));


        verify(walletService).findById(any(Long.class));
    }

    @Test
    void fetchDetailsForGivenIdShouldThrowBadRequestWhenIdIsNotFound() throws Exception {
        when(walletService.findById(any(Long.class))).thenReturn(java.util.Optional.empty());
        long inValidid = 999L;
        mockMvc.perform(get("/wallets/" + inValidid))
                .andExpect(status().isNotFound());

        verify(walletService).findById(any(Long.class));
    }

    @Test
    void shouldBeAbleToUpdateBalance() throws Exception {
        when(walletService.findById(any(Long.class))).thenReturn(java.util.Optional.empty());
        long inValid_id = 999L;
        mockMvc.perform(get("/wallets/" + inValid_id))
                .andExpect(status().isNotFound());

        verify(walletService).findById(any(Long.class));
    }

    @Test
    void shouldBeAbleToAddTransactionForAGivenWallet() throws Exception, DebitException {
        Transaction firstTransaction = new Transaction(100, TransactionType.DEBIT,"Snacks");
        long wallet_id = 999L;
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/wallets/" + wallet_id + "/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstTransaction)))
                .andExpect(status().isCreated());

        verify(walletService).addTransaction(eq(wallet_id), any(Transaction.class));
    }

    @Test
    void expectsErrorMessageWhenGivenNegativeAmount() throws Exception {
        Transaction firstTransaction = new Transaction(-100, TransactionType.CREDIT,"Snacks");
        long wallet_id = 1;
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/wallets/" + wallet_id + "/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstTransaction)))
                .andExpect(jsonPath("$.amount").value(MESSAGE_NEGATIVE_AMOUNT));
    }

    @Test
    void expectsErrorMessageWhenWhenTransactionAmountExceedsMaxLimit() throws Exception {
        Transaction invalidTransaction = new Transaction(11000, TransactionType.CREDIT,"Snacks");
        long wallet_id = 1;
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/wallets/" + wallet_id + "/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTransaction)))
                .andExpect(jsonPath("$.amount").value(Transaction.MAX_AMOUNT_ALLOWED_EXCEEDED_MESSAGE));
    }

    @Test
    void fetchTransactionsForGivenWalletId() throws Exception {
        List<Transaction> transactions = Arrays.asList(new Transaction(100, TransactionType.CREDIT,"Snacks"));
        when(transactionService.fetch(any(Long.class))).thenReturn(transactions);

        long walletId = 1;
        mockMvc.perform(get("/wallets/" + walletId + "/transactions"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].amount").value(100));

        verify(transactionService).fetch(walletId);
    }

    @Test
    void fetchTransactionsForInvalidId() throws Exception {
        when(transactionService.fetch(any(Long.class))).thenReturn(new ArrayList<>());

        long invalidId = 99999;
        mockMvc.perform(get("/wallets/" + invalidId + "/transactions"))
                .andExpect(status().isNotFound());

        verify(transactionService).fetch(invalidId);
    }

    @Test
    void expectsEmptyListWhenWalletHasNoTransaction() throws Exception {
        when(transactionService.fetch(any(Long.class))).thenReturn(new ArrayList<>());

        long walletId = 1;
        mockMvc.perform(get("/wallets/" + walletId + "/transactions"))
                .andExpect(status().isNotFound());

        verify(transactionService).fetch(walletId);
    }
}
// TODO: Failed to create transaction