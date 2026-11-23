
package com.bank.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.bank.dto.ResponseStructure;
import com.bank.entity.Address;
import com.bank.exception.IdNotFoundException;
import com.bank.repository.AddressRepository;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    
    public ResponseStructure<List<Address>> saveAllAddresses(
            List<Address> addresses) {

        List<Address> savedAddresses =
                addressRepository.saveAll(addresses);

        ResponseStructure<List<Address>> res =
                new ResponseStructure<>();

        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Address records saved successfully");
        res.setData(savedAddresses);

        return res;
    }


    // 1. Get Address By ID
    public ResponseStructure<Address> getAddressById(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                    new IdNotFoundException("Address not found with ID: " + id));

        ResponseStructure<Address> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Address fetched successfully");
        res.setData(address);

        return res;
    }

    // 2. Update Address
    public ResponseStructure<Address> updateAddress(Long id, Address address) {

        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() ->
                    new IdNotFoundException("Address not found with ID: " + id));

        existingAddress.setStreet(address.getStreet());
        existingAddress.setCity(address.getCity());
        existingAddress.setState(address.getState());
        existingAddress.setPinCode(address.getPinCode());

        Address updatedAddress = addressRepository.save(existingAddress);

        ResponseStructure<Address> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Address updated successfully");
        res.setData(updatedAddress);

        return res;
    }

    // 3. Get Addresses By City
    public ResponseStructure<List<Address>> getAddressesByCity(String city) {

        List<Address> addresses =
                addressRepository.findByCityIgnoreCase(city);

        ResponseStructure<List<Address>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Addresses fetched by city successfully");
        res.setData(addresses);

        return res;
    }

    // 4. Get Addresses By Street And City
    public ResponseStructure<List<Address>> getAddressesByStreetAndCity(
            String street, String city) {

        List<Address> addresses =
                addressRepository.findByStreetIgnoreCaseAndCityIgnoreCase(
                        street, city);

        ResponseStructure<List<Address>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Addresses fetched by street and city successfully");
        res.setData(addresses);

        return res;
    }

    // 5. Get Addresses By PIN Code
    public ResponseStructure<List<Address>> getAddressesByPinCode(String pinCode) {

        List<Address> addresses =
                addressRepository.findByPinCode(pinCode);

        ResponseStructure<List<Address>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Addresses fetched by PIN code successfully");
        res.setData(addresses);

        return res;
    }

    // 6. Get Address By Bank ID
    public ResponseStructure<List<Address>> getAddressByBankId(Long bankId) {

        List<Address> addresses =
                addressRepository.findByBank_Id(bankId);

        ResponseStructure<List<Address>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Addresses fetched by bank ID successfully");
        res.setData(addresses);

        return res;
    }
}


