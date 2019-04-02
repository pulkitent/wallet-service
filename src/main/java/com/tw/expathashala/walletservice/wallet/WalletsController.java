package com.tw.expathashala.walletservice.wallet;

import com.tw.expathashala.walletservice.transaction.Transaction;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/wallets")
@Api(value = "WalletDetails", description = "Operations performed on wallet to return details ")
class WalletsController {

    private WalletService walletService;

    WalletsController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    Wallet create(@RequestBody Wallet wallet) {
        return walletService.save(wallet);
    }

    @GetMapping("/{id}")
    Wallet fetchDetailsById(@PathVariable Long id) {
        return walletService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    Transaction postTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        return walletService.addTransaction(id, transaction);
    }

}
