package com.tw.expathashala.walletservice.wallet;

import com.tw.expathashala.walletservice.transaction.Transaction;
import com.tw.expathashala.walletservice.transaction.TransactionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/wallets")
@Api(value = "WalletDetails", description = "Operations performed on wallet to return details ")
class WalletsController {

    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

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

    @GetMapping("/{walletId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    List<Transaction> fetchTransactions(@PathVariable Long walletId) {
        return transactionService.fetch(walletId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleException(MethodArgumentNotValidException ex, HttpServletResponse response) {
        Map<String, String> errors = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
