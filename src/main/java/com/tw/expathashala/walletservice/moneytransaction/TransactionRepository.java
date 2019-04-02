package com.tw.expathashala.walletservice.moneytransaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<MoneyTransaction,Long> {
}
