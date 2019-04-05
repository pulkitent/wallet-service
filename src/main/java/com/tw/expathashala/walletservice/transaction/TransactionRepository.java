package com.tw.expathashala.walletservice.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM transaction WHERE wallet_id = ?1", nativeQuery = true)
    Optional<List<Transaction>> findTransactionByWalletId(Long id);
}
