
package com.bank.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.dto.ResponseStructure;
import com.bank.entity.Address;
import com.bank.entity.Bank;
import com.bank.exception.IdNotFoundException;
import com.bank.exception.RecordNotFoundException;
import com.bank.repository.AddressRepository;
import com.bank.repository.BankRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final AddressRepository addressRepository;

    // Save All Banks
    public ResponseStructure<List<Bank>> saveAllBanks(List<Bank> banks) {

        List<Bank> savedBanks = bankRepository.saveAll(banks);

        ResponseStructure<List<Bank>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Bank records saved successfully");
        res.setData(savedBanks);

        return res;
    }

    // 1. Create Bank
    public ResponseStructure<Bank> createBank(Bank bank) {

        if (bank.getAddress() == null || bank.getAddress().getId() == null) {
            throw new IllegalArgumentException("Address ID is required");
        }

        Long addressId = bank.getAddress().getId();

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IdNotFoundException(
                        "Address not found with ID: " + addressId));

        if (bank.getIfscCode() == null || bank.getIfscCode().isBlank()) {
            throw new IllegalArgumentException("IFSC code is required");
        }

        if (bankRepository.findByIfscCode(bank.getIfscCode()).isPresent()) {
            throw new IllegalArgumentException("IFSC code already exists");
        }

        if (bank.getContactNumber() == null
                || !bank.getContactNumber().matches("[0-9]{10}")) {
            throw new IllegalArgumentException(
                    "Contact number must contain exactly 10 digits");
        }

        if (bankRepository.findByContactNumber(bank.getContactNumber()).isPresent()) {
            throw new IllegalArgumentException("Contact number already exists");
        }

        bank.setAddress(address);

        Bank savedBank = bankRepository.save(bank);

        ResponseStructure<Bank> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Bank record saved successfully");
        res.setData(savedBank);

        return res;
    }

    // 2. Get All Banks
    public ResponseStructure<List<Bank>> getAllBanks() {

        List<Bank> banks = bankRepository.findAll();

        if (banks.isEmpty()) {
            throw new RecordNotFoundException("No bank records found");
        }

        ResponseStructure<List<Bank>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("All bank records fetched successfully");
        res.setData(banks);

        return res;
    }

    // 3. Get Bank By ID
    public ResponseStructure<Bank> getBankById(Long id) {

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(
                        "Bank not found with ID: " + id));

        ResponseStructure<Bank> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Bank record fetched successfully");
        res.setData(bank);

        return res;
    }

    // 4. Delete Bank
    @Transactional
    public ResponseStructure<String> deleteBank(Long id) {

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(
                        "Bank not found with ID: " + id));

        if (bank.getAccounts() != null && !bank.getAccounts().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete a bank that has accounts");
        }

        bankRepository.delete(bank);

        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Bank record deleted successfully");
        res.setData("Deleted bank ID: " + id);

        return res;
    }

    // 5. Get Bank By IFSC
    public ResponseStructure<Bank> getBankByIfsc(String ifscCode) {

        Bank bank = bankRepository.findByIfscCode(ifscCode)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Bank not found with IFSC: " + ifscCode));

        ResponseStructure<Bank> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Bank fetched by IFSC successfully");
        res.setData(bank);

        return res;
    }

    // 6. Get Bank By Address ID
    public ResponseStructure<List<Bank>> getBankByAddressId(Long addressId) {

        List<Bank> banks = bankRepository.findByAddress_Id(addressId);

        if (banks.isEmpty()) {
            throw new RecordNotFoundException(
                    "No banks found for address ID: " + addressId);
        }

        ResponseStructure<List<Bank>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Banks fetched by address ID successfully");
        res.setData(banks);

        return res;
    }

    // 7. Get Bank By City
    public ResponseStructure<List<Bank>> getBankByCity(String city) {

        List<Bank> banks = bankRepository.findByAddress_CityIgnoreCase(city);

        if (banks.isEmpty()) {
            throw new RecordNotFoundException(
                    "No banks found in city: " + city);
        }

        ResponseStructure<List<Bank>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Banks fetched by city successfully");
        res.setData(banks);

        return res;
    }

    // 8. Get Bank By Contact Number
    public ResponseStructure<Bank> getBankByContact(String contactNumber) {

        Bank bank = bankRepository.findByContactNumber(contactNumber)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Bank not found with contact: " + contactNumber));

        ResponseStructure<Bank> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Bank fetched by contact successfully");
        res.setData(bank);

        return res;
    }

    // 9. Get Bank By Pagination And Sorting
    public ResponseStructure<Page<Bank>> getBankByPaginationAndSorting(
            int page, int size, String sortBy) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException(
                    "Page must be >= 0 and size must be > 0");
        }

        if (sortBy == null || !List.of(
                "id", "bankName", "ifscCode", "branchName", "contactNumber")
                .contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sorting field: " + sortBy);
        }

        Pageable pageable = PageRequest.of(
                page, size, Sort.by(sortBy).ascending());

        Page<Bank> banks = bankRepository.findAll(pageable);

        if (banks.isEmpty()) {
            throw new RecordNotFoundException(
                    "No banks found on the requested page");
        }

        ResponseStructure<Page<Bank>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Banks fetched with pagination successfully");
        res.setData(banks);

        return res;
    }

    // 10. Update Bank Contact Number
    public ResponseStructure<Bank> updateBankContactNumber(
            Long id, String contactNumber) {

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(
                        "Bank not found with ID: " + id));

        if (contactNumber == null || !contactNumber.matches("[0-9]{10}")) {
            throw new IllegalArgumentException(
                    "Contact number must contain exactly 10 digits");
        }

        bankRepository.findByContactNumber(contactNumber)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Contact number already belongs to another bank");
                });

        bank.setContactNumber(contactNumber);

        Bank updatedBank = bankRepository.save(bank);

        ResponseStructure<Bank> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Bank contact number updated successfully");
        res.setData(updatedBank);

        return res;
    }
}
