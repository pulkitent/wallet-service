package com.tw.expathashala.walletservice.wallet.transaction;

import com.tw.expathashala.walletservice.transaction.Transaction;
import com.tw.expathashala.walletservice.transaction.TransactionType;
import com.tw.expathashala.walletservice.wallet.Wallet;
import com.tw.expathashala.walletservice.wallet.WalletService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    private static Validator validator;

    @Autowired
    private WalletService walletRepository;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void expectsAmountViolationWhenGivenNegativeTenAmount() {
        Transaction invalidTransaction = new Transaction(-10, TransactionType.CREDIT);

        Set<ConstraintViolation<Transaction>> violations = validator.validate(invalidTransaction);

        assertFalse(violations.isEmpty());
    }

    @Test
    void expectsNoViolationWhenGivenPositiveTenAmount() {
        Transaction validTransaction = new Transaction(10, TransactionType.CREDIT);

        Set<ConstraintViolation<Transaction>> violations = validator.validate(validTransaction);

        assertTrue(violations.isEmpty());
    }

    @Test
    void expectsAmountViolationWhenAmountExceedsMaxLimit() {
        Transaction invalidTransaction = new Transaction(11000, TransactionType.CREDIT);

        Set<ConstraintViolation<Transaction>> violations = validator.validate(invalidTransaction);

        assertFalse(violations.isEmpty());
    }

    @Test
    void expectsNoViolationWhenGiven100Amount() {
        Transaction validTransaction = new Transaction(100, TransactionType.CREDIT);

        Set<ConstraintViolation<Transaction>> violations = validator.validate(validTransaction);

        assertTrue(violations.isEmpty());
    }

    @Test
    void expectsTransactionToHaveRemarkWhenCreated(){
        Transaction transaction = new Transaction(100,TransactionType.CREDIT,"Snacks");

        assertEquals("Snacks",transaction.getRemark());
    }
}

// TODO: check violation message