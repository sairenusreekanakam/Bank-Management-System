
package com.bank.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dto.ResponseStructure;
import com.bank.entity.Bank;
import com.bank.service.BankService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    // Save All Banks
    @PostMapping("/all")
    public ResponseEntity<ResponseStructure<List<Bank>>> saveAllBanks(
            @RequestBody List<Bank> banks) {

        return new ResponseEntity<>(
                bankService.saveAllBanks(banks),
                HttpStatus.CREATED);
    }

    // 1. Create Bank
    @PostMapping
    public ResponseEntity<ResponseStructure<Bank>> createBank(
            @RequestBody Bank bank) {

        return new ResponseEntity<>(
                bankService.createBank(bank),
                HttpStatus.CREATED);
    }

    // 2. Get All Banks
    @GetMapping("/all")
    public ResponseEntity<ResponseStructure<List<Bank>>> getAllBanks() {

        return new ResponseEntity<>(
                bankService.getAllBanks(),
                HttpStatus.OK);
    }

    // 3. Get Bank By ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Bank>> getBankById(
            @PathVariable Long id) {

        return new ResponseEntity<>(
                bankService.getBankById(id),
                HttpStatus.OK);
    }

    // 4. Delete Bank
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteBank(
            @PathVariable Long id) {

        return new ResponseEntity<>(
                bankService.deleteBank(id),
                HttpStatus.OK);
    }

    // 5. Get Bank By IFSC
    @GetMapping("/ifsc/{ifscCode}")
    public ResponseEntity<ResponseStructure<Bank>> getBankByIfsc(
            @PathVariable String ifscCode) {

        return new ResponseEntity<>(
                bankService.getBankByIfsc(ifscCode),
                HttpStatus.OK);
    }

    // 6. Get Bank By Address ID
    @GetMapping("/address/{addressId}")
    public ResponseEntity<ResponseStructure<List<Bank>>> getBankByAddressId(
            @PathVariable Long addressId) {

        return new ResponseEntity<>(
                bankService.getBankByAddressId(addressId),
                HttpStatus.OK);
    }

    // 7. Get Bank By City
    @GetMapping("/city/{city}")
    public ResponseEntity<ResponseStructure<List<Bank>>> getBankByCity(
            @PathVariable String city) {

        return new ResponseEntity<>(
                bankService.getBankByCity(city),
                HttpStatus.OK);
    }

    // 8. Get Bank By Contact Number
    @GetMapping("/contact/{contactNumber}")
    public ResponseEntity<ResponseStructure<Bank>> getBankByContact(
            @PathVariable String contactNumber) {

        return new ResponseEntity<>(
                bankService.getBankByContact(contactNumber),
                HttpStatus.OK);
    }

    // 9. Get Bank By Pagination And Sorting
    @GetMapping("/page")
    public ResponseEntity<ResponseStructure<Page<Bank>>> getBankByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        return new ResponseEntity<>(
                bankService.getBankByPaginationAndSorting(page, size, sortBy),
                HttpStatus.OK);
    }

    // 10. Update Bank Contact Number
    @PatchMapping("/{id}/contact")
    public ResponseEntity<ResponseStructure<Bank>> updateBankContactNumber(
            @PathVariable Long id,
            @RequestParam String contactNumber) {

        return new ResponseEntity<>(
                bankService.updateBankContactNumber(id, contactNumber),
                HttpStatus.OK);
    }
}
