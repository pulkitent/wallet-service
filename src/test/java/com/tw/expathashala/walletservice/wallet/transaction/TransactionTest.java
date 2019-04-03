package com.tw.expathashala.walletservice.wallet.transaction;

import com.tw.expathashala.walletservice.transaction.Transaction;
import com.tw.expathashala.walletservice.transaction.TransactionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionTest {
    private static Validator validator;

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
}

// TODO: check violation message