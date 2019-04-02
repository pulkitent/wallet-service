package com.tw.expathashala.walletservice.wallet.moneyTransaction;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    WalletService walletService;

    @Test
    void updateForGivenIdShouldReturnBalance() throws Exception {
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

}
