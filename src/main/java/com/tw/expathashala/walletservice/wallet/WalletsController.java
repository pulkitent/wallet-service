package com.tw.expathashala.walletservice.wallet;

import com.tw.expathashala.walletservice.transaction.Transaction;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
    Transaction createTransaction(@PathVariable Long id, @Valid @RequestBody Transaction transaction) {
        return walletService.addTransaction(id, transaction);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Unable to add Transaction");
        return errors;
    }
}
