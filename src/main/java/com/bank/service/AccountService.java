
package com.bank.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.dto.ResponseStructure;
import com.bank.entity.Account;
import com.bank.entity.AccountType;
import com.bank.entity.Bank;
import com.bank.exception.IdNotFoundException;
import com.bank.repository.AccountRepository;
import com.bank.repository.BankRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    
 
    
 // 1. Save All Accounts
    public ResponseStructure<List<Account>> saveAllAccounts(List<Account> accounts) {

        List<Account> savedAccounts = accountRepository.saveAll(accounts);

        ResponseStructure<List<Account>> res = new ResponseStructure<>();

        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("All account records saved successfully");
        res.setData(savedAccounts);

        return res;
    }

    // Minimum balance examples - change according to your requirements
    private static final Map<String, Double> MINIMUM_BALANCE = Map.of(
            "SAVINGS", 1000.0,
            "CURRENT", 5000.0,
            "FIXED_DEPOSIT", 10000.0
    );

    // 1. Create Account
    public ResponseStructure<Account> createAccount(Account account) {

        if (account.getAccountNumber() == null
                || account.getAccountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Account number cannot be empty");
        }

        if (accountRepository.findByAccountNumber(
                account.getAccountNumber()).isPresent()) {
            throw new IllegalArgumentException(
                    "Account number must be unique");
        }

        if (account.getAccountType() == null) {
            throw new IllegalArgumentException(
                    "Account type is required");
        }

        String type = account.getAccountType().name();

        if (!MINIMUM_BALANCE.containsKey(type)) {
            throw new IllegalArgumentException(
                    "Account type must be SAVINGS, CURRENT or FIXED_DEPOSIT");
        }

        if (account.getBalance() == null
                || account.getBalance() < MINIMUM_BALANCE.get(type)) {
            throw new IllegalArgumentException(
                    "Minimum balance for " + type + " is "
                            + MINIMUM_BALANCE.get(type));
        }

        if (account.getBank() == null
                || account.getBank().getId() == null) {
            throw new IllegalArgumentException("Bank ID is required");
        }

        Long bankId = account.getBank().getId();

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new IdNotFoundException(
                        "Bank not found with ID: " + bankId));

        account.setBank(bank);
        account.setAccountType(AccountType.valueOf(type));
        Account savedAccount = accountRepository.save(account);

        ResponseStructure<Account> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Account created successfully");
        res.setData(savedAccount);
        return res;
    }

    // 2. Get All Accounts
    public ResponseStructure<List<Account>> getAllAccount() {

        ResponseStructure<List<Account>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("All accounts fetched successfully");
        res.setData(accountRepository.findAll());
        return res;
    }

    // 3. Get Account By ID
    public ResponseStructure<Account> getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(
                        "Account not found with ID: " + id));

        ResponseStructure<Account> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Account fetched successfully");
        res.setData(account);
        return res;
    }

    // 4. Delete Account
    public ResponseStructure<String> deleteAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(
                        "Account not found with ID: " + id));

        accountRepository.delete(account);

        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Account deleted successfully");
        res.setData("Deleted account ID: " + id);
        return res;
    }

    // 5. Deposit Amount
    @Transactional
    public ResponseStructure<Account> depositAmount(
            Long id, Double amount) {

        validatePositiveAmount(amount, "Deposit");

        Account account = findAccount(id);
        double currentBalance = getCurrentBalance(account);

        account.setBalance(currentBalance + amount);

        Account updatedAccount = accountRepository.save(account);

        ResponseStructure<Account> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Amount deposited successfully");
        res.setData(updatedAccount);
        return res;
    }

    // 6. Withdraw Amount
    @Transactional
    public ResponseStructure<Account> withdrawAmount(
            Long id, Double amount) {

        validatePositiveAmount(amount, "Withdrawal");

        Account account = findAccount(id);
        double currentBalance = getCurrentBalance(account);
        double minimumBalance = getMinimumBalance(account);

        if (currentBalance - amount < minimumBalance) {
            throw new IllegalArgumentException(
                    "Insufficient balance. Minimum balance to maintain is "
                            + minimumBalance);
        }

        account.setBalance(currentBalance - amount);

        Account updatedAccount = accountRepository.save(account);

        ResponseStructure<Account> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Amount withdrawn successfully");
        res.setData(updatedAccount);
        return res;
    }

    // 7. Transfer Amount
    @Transactional
    public ResponseStructure<String> transferAmount(
            Long senderId, Long receiverId, Double amount) {

        validatePositiveAmount(amount, "Transfer");

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException(
                    "Sender and receiver accounts must not be the same");
        }

        Account sender = findAccount(senderId);
        Account receiver = findAccount(receiverId);

        double senderBalance = getCurrentBalance(sender);
        double receiverBalance = getCurrentBalance(receiver);
        double senderMinimum = getMinimumBalance(sender);

        if (senderBalance - amount < senderMinimum) {
            throw new IllegalArgumentException(
                    "Insufficient balance for transfer. Minimum balance to maintain is "
                            + senderMinimum);
        }

        sender.setBalance(senderBalance - amount);
        receiver.setBalance(receiverBalance + amount);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Amount transferred successfully");
        res.setData("Transfer completed successfully");
        return res;
    }

    // 8. Update Account Holder Name
    public ResponseStructure<Account> updateAccountHolderName(
            Long id, String accountHolderName) {

        if (accountHolderName == null
                || accountHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Account holder name cannot be empty");
        }

        Account account = findAccount(id);
        account.setAccountHolderName(accountHolderName.trim());

        Account updatedAccount = accountRepository.save(account);

        ResponseStructure<Account> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Account holder name updated successfully");
        res.setData(updatedAccount);
        return res;
    }

    // 9. Get Accounts By Bank
    public ResponseStructure<List<Account>> getAccountByBank(
            Long bankId) {

        if (!bankRepository.existsById(bankId)) {
            throw new IdNotFoundException(
                    "Bank not found with ID: " + bankId);
        }

        ResponseStructure<List<Account>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Accounts fetched by bank successfully");
        res.setData(accountRepository.findByBank_Id(bankId));
        return res;
    }

    // 10. Get Accounts By Type
    public ResponseStructure<List<Account>> getAccountByType(
            String accountType) {

        if (accountType == null
                || !MINIMUM_BALANCE.containsKey(accountType.toUpperCase())) {
            throw new IllegalArgumentException("Invalid account type");
        }

        ResponseStructure<List<Account>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Accounts fetched by type successfully");
        res.setData(accountRepository.findByAccountType(
                accountType.toUpperCase()));
        return res;
    }

    // 11. Get Account By Account Number
    public ResponseStructure<Account> getAccountByAccountNumber(
            String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IdNotFoundException(
                        "Account not found with number: " + accountNumber));

        ResponseStructure<Account> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Account fetched successfully");
        res.setData(account);
        return res;
    }

    // 12. Get Accounts By Balance Value
    public ResponseStructure<List<Account>> getAccountWithBalanceValue(
            Double balanceValue) {

        if (balanceValue == null || balanceValue < 0) {
            throw new IllegalArgumentException(
                    "Balance value cannot be negative");
        }

        ResponseStructure<List<Account>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Accounts fetched by balance successfully");
        res.setData(
                accountRepository.findByBalanceGreaterThanEqual(balanceValue));
        return res;
    }

    // 13. Get Accounts By Sorting
    public ResponseStructure<List<Account>> getAccountBySorting(
            String sortBy) {

        if (sortBy == null
                || !(sortBy.equals("id")
                || sortBy.equals("accountNumber")
                || sortBy.equals("accountHolderName")
                || sortBy.equals("accountType")
                || sortBy.equals("balance"))) {
            throw new IllegalArgumentException(
                    "Sort by id, accountNumber, accountHolderName, accountType or balance");
        }

        List<Account> accounts = accountRepository.findAll(
                Sort.by(Sort.Direction.ASC, sortBy));

        ResponseStructure<List<Account>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Accounts sorted successfully");
        res.setData(accounts);
        return res;
    }

    // Helper: Find Account
    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(
                        "Account not found with ID: " + id));
    }

    // Helper: Validate Amount
    private void validatePositiveAmount(Double amount, String operation) {
        if (amount == null || !Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    operation + " amount must be greater than zero");
        }
    }

    // Helper: Get Balance
    private double getCurrentBalance(Account account) {
        if (account.getBalance() == null) {
            return 0.0;
        }
        return account.getBalance();
    }

    // Helper: Get Minimum Balance
 // Helper: Get Minimum Balance
    private double getMinimumBalance(Account account) {

        AccountType type = account.getAccountType();

        if (type == null || !MINIMUM_BALANCE.containsKey(type.name())) {
            throw new IllegalArgumentException(
                    "Invalid account type for this account");
        }

        return MINIMUM_BALANCE.get(type.name());
    }
}
