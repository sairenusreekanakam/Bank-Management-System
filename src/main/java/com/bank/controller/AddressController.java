




package com.bank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.bank.dto.ResponseStructure;
import com.bank.entity.Address;
import com.bank.service.AddressService;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    
    @PostMapping("/address/all")
    public ResponseStructure<List<Address>> saveAllAddresses(
            @RequestBody List<Address> addresses) {

        return addressService.saveAllAddresses(addresses);
    }

    // 1. Get Address By ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Address>> getAddressById(
            @PathVariable Long id) {

        return new ResponseEntity<>(
                addressService.getAddressById(id), HttpStatus.OK);
    }

    // 2. Update Address
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Address>> updateAddress(
            @PathVariable Long id,
            @RequestBody Address address) {

        return new ResponseEntity<>(
                addressService.updateAddress(id, address), HttpStatus.OK);
    }

    // 3. Get Addresses By City
    @GetMapping("/city/{city}")
    public ResponseEntity<ResponseStructure<List<Address>>> getAddressesByCity(
            @PathVariable String city) {

        return new ResponseEntity<>(
                addressService.getAddressesByCity(city), HttpStatus.OK);
    }

    // 4. Get Addresses By Street And City
    @GetMapping("/street-city")
    public ResponseEntity<ResponseStructure<List<Address>>> getAddressesByStreetAndCity(
            @RequestParam String street,
            @RequestParam String city) {

        return new ResponseEntity<>(
                addressService.getAddressesByStreetAndCity(street, city),
                HttpStatus.OK);
    }

    // 5. Get Addresses By PIN Code
    @GetMapping("/pincode/{pinCode}")
    public ResponseEntity<ResponseStructure<List<Address>>> getAddressesByPinCode(
            @PathVariable String pinCode) {

        return new ResponseEntity<>(
                addressService.getAddressesByPinCode(pinCode), HttpStatus.OK);
    }

    // 6. Get Address By Bank ID
    @GetMapping("/bank/{bankId}")
    public ResponseEntity<ResponseStructure<List<Address>>> getAddressByBankId(
            @PathVariable Long bankId) {

        return new ResponseEntity<>(
                addressService.getAddressByBankId(bankId), HttpStatus.OK);
    }
}
