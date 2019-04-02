package com.tw.expathashala.walletservice.wallet.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.expathashala.walletservice.moneytransaction.MoneyTransaction;
import com.tw.expathashala.walletservice.moneytransaction.TransactionType;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class WalletsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private
    WalletService walletService;

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
    void shouldBeAbleToAddTransactionForAGivenWallet() throws Exception {
        MoneyTransaction firstTransaction = new MoneyTransaction(100, TransactionType.DEBIT);
        long wallet_id = 999L;
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/wallets/" + wallet_id + "/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstTransaction)))
                        .andExpect(status().isCreated());

        verify(walletService).addTransaction(eq(wallet_id),any(MoneyTransaction.class));
    }

}
