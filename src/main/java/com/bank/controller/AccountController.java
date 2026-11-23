
package com.bank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.dto.ResponseStructure;
import com.bank.entity.Account;
import com.bank.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    // 1. Save All Accounts
    @PostMapping("/all")
    public ResponseEntity<ResponseStructure<List<Account>>> saveAllAccounts(
            @RequestBody List<Account> accounts) {

        return new ResponseEntity<>(
                accountService.saveAllAccounts(accounts),
                HttpStatus.CREATED);
    }

    // 1. Create Account
    @PostMapping
    public ResponseEntity<ResponseStructure<Account>> createAccount(
            @RequestBody Account account) {

        return new ResponseEntity<>(
                accountService.createAccount(account),
                HttpStatus.CREATED);
    }

    // 2. Get All Accounts
    @GetMapping("/all")
    public ResponseEntity<ResponseStructure<List<Account>>> getAllAccount() {

        return new ResponseEntity<>(
                accountService.getAllAccount(),
                HttpStatus.OK);
    }

    // 3. Get Account By ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Account>> getAccountById(
            @PathVariable Long id) {

        return new ResponseEntity<>(
                accountService.getAccountById(id),
                HttpStatus.OK);
    }

    // 4. Delete Account
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteAccount(
            @PathVariable Long id) {

        return new ResponseEntity<>(
                accountService.deleteAccount(id),
                HttpStatus.OK);
    }

    // 5. Deposit Amount
    @PatchMapping("/{id}/deposit")
    public ResponseEntity<ResponseStructure<Account>> depositAmount(
            @PathVariable Long id,
            @RequestParam Double amount) {

        return new ResponseEntity<>(
                accountService.depositAmount(id, amount),
                HttpStatus.OK);
    }

    // 6. Withdraw Amount
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<ResponseStructure<Account>> withdrawAmount(
            @PathVariable Long id,
            @RequestParam Double amount) {

        return new ResponseEntity<>(
                accountService.withdrawAmount(id, amount),
                HttpStatus.OK);
    }

    // 7. Transfer Amount
    @PostMapping("/transfer")
    public ResponseEntity<ResponseStructure<String>> transferAmount(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam Double amount) {

        return new ResponseEntity<>(
                accountService.transferAmount(
                        senderId, receiverId, amount),
                HttpStatus.OK);
    }

    // 8. Update Account Holder Name
    @PatchMapping("/{id}/holder")
    public ResponseEntity<ResponseStructure<Account>> updateAccountHolderName(
            @PathVariable Long id,
            @RequestParam String accountHolderName) {

        return new ResponseEntity<>(
                accountService.updateAccountHolderName(
                        id, accountHolderName),
                HttpStatus.OK);
    }

    // 9. Get Accounts By Bank
    @GetMapping("/bank/{bankId}")
    public ResponseEntity<ResponseStructure<List<Account>>> getAccountByBank(
            @PathVariable Long bankId) {

        return new ResponseEntity<>(
                accountService.getAccountByBank(bankId),
                HttpStatus.OK);
    }

    // 10. Get Accounts By Type
    @GetMapping("/type/{accountType}")
    public ResponseEntity<ResponseStructure<List<Account>>> getAccountByType(
            @PathVariable String accountType) {

        return new ResponseEntity<>(
                accountService.getAccountByType(accountType),
                HttpStatus.OK);
    }

    // 11. Get Account By Account Number
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<ResponseStructure<Account>>
            getAccountByAccountNumber(
                    @PathVariable String accountNumber) {

        return new ResponseEntity<>(
                accountService.getAccountByAccountNumber(accountNumber),
                HttpStatus.OK);
    }

    // 12. Get Accounts By Balance Value
    @GetMapping("/balance")
    public ResponseEntity<ResponseStructure<List<Account>>>
            getAccountWithBalanceValue(
                    @RequestParam Double balanceValue) {

        return new ResponseEntity<>(
                accountService.getAccountWithBalanceValue(balanceValue),
                HttpStatus.OK);
    }

    // 13. Get Accounts By Sorting
    @GetMapping("/sort")
    public ResponseEntity<ResponseStructure<List<Account>>>
            getAccountBySorting(
                    @RequestParam(defaultValue = "id") String sortBy) {

        return new ResponseEntity<>(
                accountService.getAccountBySorting(sortBy),
                HttpStatus.OK);
    }
}
