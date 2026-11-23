
package com.bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 1. Find Account By Account Number
    Optional<Account> findByAccountNumber(String accountNumber);

    // 2. Find Accounts By Bank ID
    List<Account> findByBank_Id(Long bankId);

    // 3. Find Accounts By Account Type
    List<Account> findByAccountType(String accountType);

    // 4. Find Accounts By Minimum Balance
    List<Account> findByBalanceGreaterThanEqual(Double balance);

}
